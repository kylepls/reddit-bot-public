package in.kyle.yt.redditbot.reddit.model;

import java.util.Set;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class RedditThread extends RedditContent {

  String mutatedTitle;
  boolean over18;
  @Singular Set<RedditAward> awards;
  String flairText;

  @Builder
  public RedditThread(
          Subreddit subreddit,
          int score,
          String content,
          String author,
          boolean over18,
          String link,
          @Singular Set<RedditAward> awards,
          String identifier, String mutatedTitle, String flairText) {
    super(identifier, author, score, content, subreddit, link);
    this.over18 = over18;
    this.awards = awards;
    this.mutatedTitle = mutatedTitle;
    this.flairText = flairText;
  }

  public String getTitle() {
    return getContent();
  }
}
