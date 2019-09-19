package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import in.kyle.yt.redditbot.render.TimelineRenderer;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.TimelinePrinter;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.TrackElement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FfmpegTimelineConverter implements TimelineRenderer {
  
  private final FfmpegSettings settings;
  private final FfmpegRenderer renderer;
  private final TimelinePrinter timelinePrinter;
  
  @Override
  @SneakyThrows
  public void render(Timeline timeline, Path outputFile) {
    log.info("Rendering timeline: \n{}", timelinePrinter.printTimeline(timeline));
    if (Files.exists(outputFile)) {
      Files.delete(outputFile);
    }
    var command = makeCommand(timeline, outputFile);
    renderer.run(command);
  }
  
  private FfmpegCommand makeCommand(Timeline timeline, Path outputFile) {
    var builder = FfmpegCommand.builder();
    List<String> inputArgs = new ArrayList<>();
    inputArgs.addAll(makeColorInput(timeline.getBackgroundColor(), timeline.getDimension()));
    List<FfmpegInput> inputs = makeInputs(timeline);
    inputs.stream().flatMap(i -> makeInputStrings(i).stream()).forEach(inputArgs::add);
    builder.inputArgs(inputArgs);
    
    var filterComplex = new FfmpegFilterComplex(inputs, timeline.getMaxTime().getMillis());
    builder.filterGraph(filterComplex.makeFilterGraph());
    
    List<String> postFilter = new ArrayList<>();
    postFilter.addAll(List.of("-map", String.format("[%s]", filterComplex.getResultVideoStream())));
    if (filterComplex.getResultAudioStream() != null) {
      postFilter.addAll(List.of("-map", String.format("[%s]", filterComplex.getResultAudioStream())));
    }
    
    postFilter.addAll(List.of("-t", DurationFormatUtils.formatDurationHMS(timeline.getMaxTime().getMillis())));
    postFilter.addAll(List.of("-s", timeline.getDimension().toSimpleString()));
    postFilter.addAll(List.of("-r", String.format("%.2f", settings.getFrameRate())));
    postFilter.addAll(settings.getFlagsList());
    builder.postFilterArgs(postFilter);
    
    builder.outputFile(outputFile);
    builder.totalVideoTime(timeline.getMaxTime().getMillis());
    
    return builder.build();
  }
  
  private List<FfmpegInput> makeInputs(Timeline timeline) {
    List<TrackElement> elements = getRenderOrder(timeline);
    List<FfmpegInput> inputs = new ArrayList<>();
    
    int index = 1;
    for (TrackElement element : elements) {
      FfmpegInput input = makeInput(element, index);
      inputs.add(input);
      index += 1;
    }
    
    return inputs;
  }
  
  private FfmpegInput makeInput(TrackElement element, int index) {
    return new FfmpegInput(element, element.getMedia().getFile().toString(), index);
  }
  
  private List<String> makeInputStrings(FfmpegInput input) {
    return List.of("-i", input.getFile());
  }
  
  private List<TrackElement> getRenderOrder(Timeline timeline) {
    // render lowest tracks first
    List<Track> tracks = new ArrayList<>(timeline.getTracks());
    Collections.reverse(tracks);
    return tracks.stream().flatMap(t -> t.getAllElements().stream()).collect(Collectors.toList());
  }
  
  private List<String> makeColorInput(Color color, Dimension dimension) {
    List<String> parts = new ArrayList<>();
    String colorString = String.format("0x%06x", color.getRGB() & 0x00_FFFFFF);
    String colorFilter = String.format("color=c=%s:s=%s", colorString, dimension.toSimpleString());
    parts.addAll(List.of("-f", "lavfi"));
    parts.addAll(List.of("-i", colorFilter));
    return parts;
  }
}
