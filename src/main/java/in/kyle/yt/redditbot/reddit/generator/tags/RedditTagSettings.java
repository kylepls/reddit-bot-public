package in.kyle.yt.redditbot.reddit.generator.tags;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

import lombok.Value;

@Value
@ConfigurationProperties("tags")
class RedditTagSettings {
    
    String startingTags;
    String endingTags;
    
    public List<String> getStartingTags() {
        return List.of(startingTags.trim().split(","));
    }
    
    public List<String> getEndingTags() {
        return List.of(endingTags.trim().split(","));
    }
}
