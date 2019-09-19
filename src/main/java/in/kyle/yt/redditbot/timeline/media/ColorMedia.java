package in.kyle.yt.redditbot.timeline.media;

import java.awt.*;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ColorMedia extends VisualMedia {

  private final Color color;

  public ColorMedia(Color color, Duration duration, Dimension dimension) {
    super(null, List.of(Attributes.IMAGE), "color-" + colorToString(color), duration, dimension);
    this.color = color;
  }

  private static String colorToString(Color color) {
    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
  }

  @Override
  public ColorMedia copy() {
    return new ColorMedia(color, getDuration(), getDimension());
  }
}
