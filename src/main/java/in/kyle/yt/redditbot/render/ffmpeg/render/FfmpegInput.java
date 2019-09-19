package in.kyle.yt.redditbot.render.ffmpeg.render;

import in.kyle.yt.redditbot.timeline.TrackElement;
import lombok.Value;

@Value
class FfmpegInput {

  TrackElement element;
  String file;
  int identifier;
}
