package in.kyle.yt.redditbot.reddit.scraper.filter;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.model.RedditContent;
import in.kyle.yt.redditbot.reddit.model.RedditThread;

@Component
public class FlairFilter implements ContentFilter {
    @Override
    public boolean allow(RedditContent content) {
        if (content instanceof RedditThread) {
            RedditThread thread = (RedditThread) content;
            String flairText = thread.getFlairText();
            return !flairText.equals("Breaking News") && !flairText.equals("Modpost");
        } else {
            return true;
        }
    }
}
