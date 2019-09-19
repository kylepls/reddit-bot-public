package in.kyle.yt.redditbot.tts.polly;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@ConfigurationProperties(prefix = "polly")
@Data
@Validated
class AmazonPollySettings {

  @NotEmpty private final String voiceId;

  @Min(1)
  private final float wpm;
}
