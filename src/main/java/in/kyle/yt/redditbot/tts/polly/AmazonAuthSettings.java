package in.kyle.yt.redditbot.tts.polly;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@ConfigurationProperties(prefix = "amazon.auth")
@Data
@Validated
class AmazonAuthSettings {

  @NotEmpty private final String key;
  @NotEmpty private final String secret;
}
