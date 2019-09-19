package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.model.RedditContent;

@Component
class UrlFilter implements ContentFilter {

  @Override
  public boolean allow(RedditContent comment) {
    return comment.getContentAsHtml().select("a").isEmpty();
  }
}
