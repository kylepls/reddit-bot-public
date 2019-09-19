package in.kyle.yt.redditbot.reddit.scraper;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;

import lombok.Data;

@ConfigurationProperties(prefix = "reddit.scraper")
@Data
@Validated
public class RedditScraperSettings {

  @Range(min = 0, max = 200)
  private final int threadMaxLength;

  @Min(0)
  private final int threadThresholdScore;

  @Min(0)
  private final int commentMaxLength;

  @Min(0)
  private final int commentThresholdScore;
}
