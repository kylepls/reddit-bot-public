package in.kyle.yt.redditbot.timeline;

import java.util.List;

import in.kyle.yt.redditbot.timeline.media.Media;

public class TestMedia extends Media {

  private TestMedia(String name, Duration duration, Attributes... attributes) {
    super(null, List.of(attributes), name, duration);
  }

  public static Media newVisualMedia() {
    return newMedia(Attributes.IMAGE);
  }

  public static Media newMedia(Attributes... attributes) {
    return newMedia(1000, attributes);
  }

  public static Media newMedia(long duration, Attributes... attributes) {
    return new TestMedia("test" + duration, new Duration(duration), attributes);
  }

  @Override
  public Media copy() {
    return new TestMedia(getName(), getDuration());
  }
}
