package in.kyle.yt.redditbot.uploaders.youtube;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@ConfigurationProperties(prefix = "youtube")
@Validated
@Data
class YoutubeSettings {

  @NotEmpty private final String categoryId;
  @NotEmpty private final String clientId;
  @NotEmpty private final String clientSecret;
  @NotEmpty private final String refreshToken;
  
}
