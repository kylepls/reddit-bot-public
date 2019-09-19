package in.kyle.yt.redditbot.tts.vocalware;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import lombok.Value;

@ConditionalOnProperty(value = "tts", havingValue = "vocalware")
@ConfigurationProperties(prefix = "vocalware")
@Value
@Validated
class VocalwareTtsSettings {

  @NotEmpty String eid;
  @NotEmpty String lid;
  @NotEmpty String vid;
  @NotEmpty String acc;
  @NotEmpty String api;
  @NotEmpty String secretPhrase;
}
