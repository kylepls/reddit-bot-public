package in.kyle.yt.redditbot.keyword.cortical;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@ConfigurationProperties("cortical")
@Validated
@Data
class CorticalSettings {

  @NotEmpty private final String apiKey;
}
