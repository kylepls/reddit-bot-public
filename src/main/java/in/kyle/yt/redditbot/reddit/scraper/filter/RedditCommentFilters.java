package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Service;

import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditContent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedditCommentFilters {

  private final List<ContentFilter> filters;

  public boolean allow(RedditContent content) {
    return filters.stream().allMatch(f -> f.allow(content));
  }
}
