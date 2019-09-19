package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.kyle.yt.redditbot.render.ffmpeg.render.resource.ResourceMonitor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
class FfmpegStatus {

  private final Consumer<String> nul = v -> {};

  private final Map<StatusElement, Consumer<String>> consumers = new HashMap<>();
  private final OutputStream logOutput = makeLogStream();
  private final InputStream stream;
  private final OutputStream output;
  private final long pid;
  private final ResourceMonitor resourceMonitor;

  private float lastPercent = -1;

  @SneakyThrows
  private static FileOutputStream makeLogStream() {
    return new FileOutputStream(new File("ffmpeg.log"));
  }

  void addConsumer(StatusElement type, Consumer<String> consumer) {
    consumers.put(type, consumer);
  }

  @SneakyThrows
  public void run() {
    long start = System.currentTimeMillis();

    Pattern pattern = Pattern.compile("(?<key>[a-z]+)=\\s*(?<value>\\b[^ ]+\\b)");
    Scanner scanner = new Scanner(stream);
    scanner.useDelimiter("\r");
    while (scanner.hasNext()) {
      String part = scanner.next();
      logOutput.write(part.getBytes());
      logOutput.flush();
      Matcher matcher = pattern.matcher(part);
      while (matcher.find()) {
        Optional<StatusElement> key = StatusElement.get(matcher.group("key"));
        if (key.isPresent()) {
          String value = matcher.group("value");
          consumers.getOrDefault(key.get(), nul).accept(value);
        } else {
          log.info("Unusual line {}", part);
        }
      }
    }
    long end = System.currentTimeMillis();
    writef("Finished in %s", DurationFormatUtils.formatDurationHMS(end - start));
  }

  void showStatusString(long totalTime) {
    Deque<DataPoint> points = new LimitedQueue<>(100);
    addConsumer(
        StatusElement.time,
        time -> {
          long rendered = parseDuration(time);
          float percent = rendered / (float) totalTime * 100;
          long now = System.currentTimeMillis();
          points.add(new DataPoint(now, percent));

          long remaining = linearRemainderEstimate(points);

          if (remaining > 0) {
            // Throttle messages by 1% increments to prevent log spam
            if (Math.floor(percent) > Math.floor(lastPercent + 10)) {
              ResourceMonitor.ResourceUsage usage = resourceMonitor.getUsage(pid);
              writef(
                  "Percent: %05.2f, Remaining Time: %s, Memory: %s CPU: %s",
                  percent,
                  DurationFormatUtils.formatDuration(remaining, "HH:mm:ss"),
                  usage.getMemory(),
                  usage.getCpu());
              lastPercent = percent;
            }
          }
        });
  }

  private long linearRemainderEstimate(Deque<DataPoint> dataPoints) {
    DataPoint first = dataPoints.getFirst();
    DataPoint last = dataPoints.getLast();

    long deltaT = last.getTime() - first.getTime();
    float deltaP = last.getPercent() - first.getPercent();
    float pi = last.getPercent();

    return (long) ((100 - pi) / (deltaP / deltaT));
  }

  private long parseDuration(String string) {
    return Duration.between(LocalTime.MIN, LocalTime.parse(string)).toMillis();
  }

  @SneakyThrows
  private void writef(String string, Object... args) {
    output.write(String.format(string + "\n", args).getBytes());
    output.flush();
  }

  public enum StatusElement {
    frame,
    q,
    fps,
    size,
    time,
    bitrate,
    dup,
    drop,
    speed;

    public static Optional<StatusElement> get(String name) {
      try {
        return Optional.of(valueOf(name));
      } catch (IllegalArgumentException e) {
        return Optional.empty();
      }
    }
  }

  @RequiredArgsConstructor
  static class LimitedQueue<E> extends LinkedList<E> {
    private final int limit;

    @Override
    public boolean add(E e) {
      boolean added = super.add(e);
      while (added && size() > limit) {
        super.remove();
      }
      return added;
    }
  }

  @Data
  private static class DataPoint {
    private final long time;
    private final float percent;
  }
}
