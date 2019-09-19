package in.kyle.yt.redditbot.timeline;

public class TestTrackElement {
  public static TrackElement newTrackElement() {
    return new TrackElement(TestMedia.newMedia(), new Timestamp(0));
  }

  public static TrackElement newVisualTrackElement() {
    return new VisualTrackElement(new Timestamp(0), TestMedia.newVisualMedia());
  }
}
