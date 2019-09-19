package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.TrackElement;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import in.kyle.yt.redditbot.timeline.media.ImageMedia;
import in.kyle.yt.redditbot.timeline.media.Media;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class FfmpegFilterComplex {

  private final List<FfmpegInput> inputs;
  private final long wholeDuration;
  private String resultAudioStream;
  private String resultVideoStream;

  String makeFilterGraph() {
    List<FfmpegStream> ffmpegStreams = new ArrayList<>();
    var visualStreams =
        inputs.stream()
            .filter(i -> i.getElement() instanceof VisualTrackElement)
            .map(this::makeVideoStream)
            .collect(Collectors.toList());
    ffmpegStreams.addAll(visualStreams);
    ffmpegStreams.addAll(makeOverlayReductionTree(ffmpegStreams));

    List<FfmpegStream> audioStreams =
        inputs.stream()
            .filter(i -> i.getElement().getMedia().hasAttributes(Media.Attributes.SOUND))
            .map(this::makeAudioFilterStream)
            .collect(Collectors.toList());
    if (audioStreams.size() != 0) {
      ffmpegStreams.addAll(audioStreams);
      resultAudioStream = "aout";
      ffmpegStreams.add(makeAudioMixStream(audioStreams));
    }

    return ffmpegStreams.stream().map(this::makeStreamString).collect(Collectors.joining(";\n"));
  }

  private String makeStreamString(FfmpegStream stream) {
    StringBuilder sb = new StringBuilder();
    stream.getInputs().forEach(input -> sb.append(String.format("[%s]", input)));

    if (stream.getFilters().isEmpty()) {
      sb.append(stream.hasVisual() ? "null" : "anull");
    } else {
      var filters =
          stream.getFilters().stream().map(this::makeFilterString).collect(Collectors.joining(","));
      sb.append(filters);
    }

    sb.append(String.format("[%s]", stream.getOutput()));
    return sb.toString();
  }

  String makeFilterString(FfmpegFilter filter) {
    StringBuilder sb = new StringBuilder();
    sb.append(filter.getName());
    if (!filter.getArgs().isEmpty()) {
      sb.append("=");
      var args =
          filter.getArgs().stream()
              .map(a -> String.format("%s=%s", a.getFirst(), a.getSecond()))
              .collect(Collectors.joining(":"));
      sb.append(args);
    }
    return sb.toString();
  }

  FfmpegStream makeAudioFilterStream(FfmpegInput input) {
    var builder = FfmpegStream.builder();
    TrackElement element = input.getElement();
    Timestamp start = element.getStart();

    Duration duration = element.getDuration();
    Duration startOffset = element.getMedia().getStartOffset();
    Duration endOffset = startOffset.add(duration);
    FfmpegFilter trimFilter =
        FfmpegFilter.builder()
            .name("atrim")
            .arg(Pair.of("start", String.format("%.3f", startOffset.getFractionalSeconds())))
            .arg(Pair.of("end", String.format("%.3f", endOffset.getFractionalSeconds())))
            .build();
    builder.filter(trimFilter);

    FfmpegFilter delayFilter =
        FfmpegFilter.builder()
            .name("adelay")
            .arg(Pair.of("delays", String.format("%d|%d", start.getMillis(), start.getMillis())))
            .build();
    builder.filter(delayFilter);

    FfmpegFilter padFilter =
        FfmpegFilter.builder()
            .name("apad")
            .arg(Pair.of("whole_dur", String.format("%.3f", wholeDuration / 1000F)))
            .build();
    builder.filter(padFilter);

    element.getAudioFilters().stream()
        .map(f -> new FfmpegFilter(f.getName(), f.getValues()))
        .forEach(builder::filter);

    return builder
        .input(String.format("%d:a", input.getIdentifier()))
        .output(String.format("a%d", input.getIdentifier()))
        .build();
  }

  FfmpegStream makeAudioMixStream(List<FfmpegStream> streams) {
    var builder = FfmpegStream.builder();
    for (FfmpegStream filteredAudioStream : streams) {
      builder.input(filteredAudioStream.getOutput());
    }

    builder.filter(
        FfmpegFilter.builder()
            .name("amix")
            .arg(Pair.of("inputs", String.valueOf(streams.size())))
            .build());

    builder.filter(
        FfmpegFilter.builder()
            .name("volume")
            .arg(Pair.of("volume", String.valueOf(streams.size())))
            .build());

    builder.output(resultAudioStream);

    return builder.build();
  }

  List<FfmpegStream> makeOverlayReductionTree(List<FfmpegStream> streams) {
    List<FfmpegStream> output = new ArrayList<>();
    var imageStreams =
        streams.stream().filter(FfmpegStream::hasVisual).collect(Collectors.toList());
    String lastStream = "0";
    int i = 1;
    for (FfmpegStream imageStream : imageStreams) {
      String outputStream = "o" + (i++);
      var builder = FfmpegStream.builder();
      builder.input(lastStream).input(imageStream.getOutput());
      builder.filter(makeOverlayFilter(imageStream.getVisualSource()));
      builder.output(outputStream);
      output.add(builder.build());
      lastStream = outputStream;
    }

    resultVideoStream = lastStream;

    return output;
  }

  private FfmpegFilter makeOverlayFilter(VisualTrackElement element) {
    var builder = FfmpegFilter.builder();
    builder.name("overlay").args(element.getOverlay().makeArgs());
    if (!(element.getMedia() instanceof ImageMedia)) {
      builder.arg(Pair.of("eof_action", "pass"));
    }
    String enableExpression =
        String.format(
            "'between(t, %.3f, %.3f)'",
            element.getStart().getFractionalSeconds(), element.getEnd().getFractionalSeconds());
    builder.arg(Pair.of("enable", enableExpression));
    return builder.build();
  }

  private FfmpegStream makeVideoStream(FfmpegInput input) {
    var builder =
        FfmpegStream.builder()
            .input(String.format("%d:v", input.getIdentifier()))
            .source(input.getElement())
            .output(String.format("f%d", input.getIdentifier()));
    String ptsExpr =
        String.format("PTS+%.3f/TB", input.getElement().getStart().getFractionalSeconds());
    var setptsFilter = FfmpegFilter.builder().name("setpts").arg(Pair.of("expr", ptsExpr)).build();
    builder.filter(setptsFilter);
    input.getElement().getVideoFilters().stream()
        .map(f -> new FfmpegFilter(f.getName(), f.getValues()))
        .forEach(builder::filter);
    return builder.build();
  }
}
