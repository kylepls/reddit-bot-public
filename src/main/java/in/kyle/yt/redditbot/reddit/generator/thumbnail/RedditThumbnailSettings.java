package in.kyle.yt.redditbot.reddit.generator.thumbnail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.awt.*;

import lombok.Value;

@ConfigurationProperties(prefix = "reddit.thumbnail")
@Value
class RedditThumbnailSettings {

  Resource titleFont;
  Resource subredditFont;
  float subredditOutline;
  Resource background;
  String fontColor;
  String outlineColor;
  float titleOutline;
  
  public Color getFontColor() {
    return Color.decode(fontColor);
  }
  
  public Color getOutlineColor() {
    return Color.decode(outlineColor);
  }
  
  public Stroke getSubredditOutline() {
    return new BasicStroke(subredditOutline, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }
  
  public Stroke getTitleOutline() {
    return new BasicStroke(titleOutline, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
  }
}
