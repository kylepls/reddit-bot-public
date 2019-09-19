package in.kyle.yt.redditbot.render.ffmpeg.meta;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.render.ffmpeg.render.FfmpegSettings;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.media.meta.FileMeta;
import in.kyle.yt.redditbot.timeline.media.meta.FileMetaProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
class FfmpegMetaProvider implements FileMetaProvider {

  private final FfmpegSettings settings;

  @Override
  @SneakyThrows
  @Cacheable("FileMeta")
  public FileMeta getMetadata(Path file) {
    var parts = List.of(settings.getExecutable(), "-hide_banner", "-i", file.toString());

    Process process = new ProcessBuilder().command(parts).start();
    process.waitFor();

    String output = new Scanner(process.getErrorStream()).useDelimiter("\\A").next();
    if (output.contains("Failed")) {
      String message = output.substring(output.indexOf("Failed"));
      Conditions.error("Could not read {}\n{}", file.toAbsolutePath().toString(), message);
    }

    return new FfmpegMeta(output);
  }

  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  private static class FfmpegMeta implements FileMeta {

    private final String value;

    public Dimension getDimensions() {
      Pattern pattern = Pattern.compile(", \\b(?<w>\\d+)x(?<h>\\d+)\\b");
      Matcher matcher = pattern.matcher(value);
      matcher.find();
      return new Dimension(
          Integer.parseInt(matcher.group("w")), Integer.parseInt(matcher.group("h")));
    }

    public Duration getDuration() {
      Pattern pattern =
          Pattern.compile(
              "Duration: (?<hours>\\d{2}):(?<minutes>\\d{2}):(?<seconds>\\d{2})\\."
                  + "(?<fractional>\\d{2})");
      Matcher matcher = pattern.matcher(value);
      matcher.find();
      long value = 0;
      value += TimeUnit.HOURS.toMillis(Long.parseLong(matcher.group("hours")));
      value += TimeUnit.MINUTES.toMillis(Long.parseLong(matcher.group("minutes")));
      value += TimeUnit.SECONDS.toMillis(Long.parseLong(matcher.group("seconds")));
      value += Long.parseLong(matcher.group("fractional")) * 10;
      return new Duration(value);
    }
  }
}
