package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.data.util.Pair;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
class FfmpegFilter {

  String name;
  @Singular List<Pair<String, String>> args;
}
