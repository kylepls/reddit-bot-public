package in.kyle.yt.redditbot.timeline;

import in.kyle.yt.redditbot.timeline.media.Media;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VisualTrackElement extends TrackElement {

  private Overlay overlay = new Overlay();

  VisualTrackElement(Timestamp start, Media media) {
    super(media, start);
  }

  @Override
  public VisualTrackElement copyTo(Track target, TimeRange timerange) {
    VisualTrackElement element = (VisualTrackElement) super.copyTo(target, timerange);
    element.setOverlay(getOverlay());
    return element;
  }

  public void setPosition(int x, int y) {
    setPosition(String.valueOf(x), String.valueOf(y), true);
  }

  public void setPosition(String x, String y, boolean isStatic) {
    overlay.setPosition(x, y, isStatic);
  }
}
