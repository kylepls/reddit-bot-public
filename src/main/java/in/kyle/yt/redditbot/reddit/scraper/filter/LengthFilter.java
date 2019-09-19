package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.model.RedditContent;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.scraper.RedditScraperSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class LengthFilter implements ContentFilter {

  private final RedditScraperSettings settings;

  @Override
  public boolean allow(RedditContent content) {
    if (content instanceof RedditThread) {
      int length = ((RedditThread) content).getMutatedTitle().length();
      return length <= settings.getThreadMaxLength();
    } else {
      int length = content.getContent().length();
      return length <= settings.getCommentMaxLength();
    }
  }
}
