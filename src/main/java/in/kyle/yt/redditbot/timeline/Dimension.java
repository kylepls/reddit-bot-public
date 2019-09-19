package in.kyle.yt.redditbot.timeline;

import lombok.Data;

@Data
public class Dimension {

  private final int width;
  private final int height;

  public String toSimpleString() {
    return String.format("%dx%d", width, height);
  }
}
