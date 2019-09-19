package in.kyle.yt.redditbot.reddit.generator.tags;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import in.kyle.yt.redditbot.keyword.KeywordExtractor;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedditKeywordGenerator {
    
    private final RedditKeywordSettings settings;
    private final KeywordExtractor keywordExtractor;
    
    public List<String> getKeywords(RedditThread thread) {
        List<String> keywords = keywordExtractor.getKeywords(thread.getTitle());
        List<String> result = new ArrayList<>(keywords);
        result.addAll(settings.getDefaultKeywords());
        return result;
    }
}
