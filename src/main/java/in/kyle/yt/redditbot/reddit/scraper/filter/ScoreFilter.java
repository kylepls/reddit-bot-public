package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.model.RedditContent;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.scraper.RedditScraperSettings;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class ScoreFilter implements ContentFilter {

  private final RedditScraperSettings settings;

  @Override
  public boolean allow(RedditContent content) {
    if (content instanceof RedditThread) {
      return content.getScore() > settings.getThreadThresholdScore();
    } else {
      return content.getScore() > settings.getCommentThresholdScore();
    }
  }
}
