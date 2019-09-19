package in.kyle.yt.redditbot.reddit.model;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class RedditComment extends RedditContent {

  List<RedditComment> children;

  @Builder
  public RedditComment(
      Subreddit subreddit,
      String author,
      int score,
      String content,
      @Singular List<RedditComment> children,
      String identifier,
      String link) {
    super(identifier, author, score, content, subreddit, link);
    this.children = children;
  }
}
