package in.kyle.yt.redditbot.render.ffmpeg.meta;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.render.FileValidator;
import in.kyle.yt.redditbot.render.ffmpeg.render.FfmpegSettings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class FfmpegValidator implements FileValidator {
  
  private final FfmpegSettings settings;
  
  @Override
  @SneakyThrows
  public void validate(Path file) {
    log.info("Validating {}", file);
    var command = List.of(settings.getExecutable(), "-v", "error", "-i", file.toString(), "-f", "null", "-");
    
    Process process = new ProcessBuilder().command(command).start();
    process.waitFor();
    
    InputStream kindaInputStream = process.getErrorStream();
    if (kindaInputStream.available() != 0) {
      String output = new Scanner(kindaInputStream).useDelimiter("\\A").next();
      Conditions.error("Failed to validate {} :\n{}", file, output);
    }
  }
}
