package in.kyle.yt.redditbot.reddit.model;

import org.jsoup.nodes.Element;

import in.kyle.yt.redditbot.reddit.text.TextUtils;
import lombok.Data;

@Data
public abstract class RedditContent {

  private final String identifier;
  private final String author;
  private final int score;
  private final String content;
  private final Subreddit subreddit;
  private final String link;

  public Element getContentAsHtml() {
    return TextUtils.parse(getContent());
  }

  public String getContentAsText() {
    return TextUtils.removeHtml(getContent());
  }

  public String getContent() {
    return TextUtils.unescapeEntities(content);
  }
}
