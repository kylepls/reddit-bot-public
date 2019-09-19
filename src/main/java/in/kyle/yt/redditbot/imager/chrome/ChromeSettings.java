package in.kyle.yt.redditbot.imager.chrome;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

import lombok.Data;

@ConfigurationProperties(prefix = "chrome")
@Validated
@Data
class ChromeSettings {

  @URL private final String endpoint;

  public URI getEndpoint() {
    return URI.create(endpoint);
  }
}
