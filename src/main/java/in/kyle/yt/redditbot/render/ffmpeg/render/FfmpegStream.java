package in.kyle.yt.redditbot.render.ffmpeg.render;

import java.util.List;

import in.kyle.yt.redditbot.timeline.TrackElement;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import in.kyle.yt.redditbot.timeline.media.Media;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
class FfmpegStream {

  @Singular List<String> inputs;
  String output;
  @Singular List<FfmpegFilter> filters;
  TrackElement source;

  public VisualTrackElement getVisualSource() {
    return (VisualTrackElement) source;
  }

  public boolean hasVisual() {
    return source.getMedia().hasAttributes(Media.Attributes.IMAGE);
  }

  public boolean hasSound() {
    return source.getMedia().hasAttributes(Media.Attributes.SOUND);
  }
}
