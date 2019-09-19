package in.kyle.yt.redditbot.reddit.scraper.filter;

import in.kyle.yt.redditbot.reddit.model.RedditContent;

interface ContentFilter {

  boolean allow(RedditContent content);
}
