package in.kyle.yt.redditbot.render;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import in.kyle.yt.redditbot.render.ffmpeg.ConcatRenderer;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.TimelineSplitter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncrementalTimelineRenderer {

  private final TimelineRenderer renderer;
  private final ConcatRenderer concatRenderer;
  private final TimelineSplitter splitter;

  @SneakyThrows
  public void render(Timeline timeline, Path output, Path tempFolder) {
    long start = System.currentTimeMillis();
    var files = renderTimelines(timeline, tempFolder);
    log.info("Rendering final file...");
    concatRenderer.concat(files, tempFolder, output, timeline.getMaxTime().getMillis());
    long end = System.currentTimeMillis() - start;
    log.info("Finished super render in {}", new Duration(end));
  }
  
  private List<Path> renderTimelines(Timeline timeline, Path tempFolder) throws IOException {
    var timelines = splitter.splitTimeline(timeline, new Duration("30s"));
    List<Path> outputFiles = new ArrayList<>();
    Files.createDirectories(tempFolder);
    for (int i = 0; i < timelines.size(); i++) {
      log.info("Generating timeline {}/{}", i, timelines.size());
      Timeline timelinen = timelines.get(i);
      log.info("Rendering timeline {}/{}", i, timelines.size());
      Path outputFile = tempFolder.resolve(i + ".mp4");
      renderer.render(timelinen, outputFile);
      outputFiles.add(outputFile);
    }
    return outputFiles;
  }
}
