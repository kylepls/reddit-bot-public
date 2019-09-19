package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.model.RedditContent;
import in.kyle.yt.redditbot.reddit.model.RedditThread;

@Component
class NsfwFilter implements ContentFilter {

  @Override
  public boolean allow(RedditContent content) {
    if (content instanceof RedditThread) {
      return !((RedditThread) content).isOver18();
    } else {
      return true;
    }
  }
}
