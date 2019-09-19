package in.kyle.yt.redditbot.reddit.generator.meta;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Value;

@ConfigurationProperties(prefix = "reddit.description")
@Value
class RedditVideoMetaSettings {

  Resource template;
}
