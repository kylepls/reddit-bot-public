package in.kyle.yt.redditbot.reddit.generator.images;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Value;

@ConfigurationProperties("reddit.imager")
@Value
class RedditImagerSettings {

  Resource payloadFile;
}
