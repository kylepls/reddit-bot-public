package in.kyle.yt.redditbot.render.ffmpeg.render;

import java.nio.file.Path;
import java.util.List;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class FfmpegCommand {

  List<String> inputArgs;
  String filterGraph;
  List<String> postFilterArgs;
  Path outputFile;
  long totalVideoTime;
}
