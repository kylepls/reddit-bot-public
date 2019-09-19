package in.kyle.yt.redditbot.reddit.generator.tags;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import lombok.Value;

@Value
@ConfigurationProperties("keywords")
class RedditKeywordSettings {
    
    String defaultKeywords;
    
    public List<String> getDefaultKeywords() {
        return List.of(defaultKeywords.split(" "));
    }
}
