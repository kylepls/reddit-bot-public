package in.kyle.yt.redditbot.timeline.modifier;

import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import lombok.Data;

@Data
public class AnimationModifier implements Modifier<VisualTrackElement> {

  private String fromX = "0";
  private String fromY = "0";
  private String toX = "0";
  private String toY = "0";

  public void setY(String from, String to) {
    this.fromY = from;
    this.toY = to;
  }

  public void setX(String from, String to) {
    this.fromX = from;
    this.toX = to;
  }

  public void setFrom(String x, String y) {
    this.fromX = x;
    this.fromY = y;
  }

  public void setTo(String x, String y) {
    this.toX = x;
    this.toY = y;
  }

  @Override
  public void apply(VisualTrackElement e) {
    // linear
    String animationFunction =
        String.format(
            "(t-%.4f)/%.4f",
            e.getStart().getFractionalSeconds(), e.getDuration().getFractionalSeconds());

    String x = String.format("((%s)-(%s))*(%s)+(%s)", toX, fromX, animationFunction, fromX);
    String y = String.format("((%s)-(%s))*(%s)+(%s)", toY, fromY, animationFunction, fromY);
    e.getOverlay().setPosition(x, y, false);
  }
}
