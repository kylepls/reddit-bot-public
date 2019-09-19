package in.kyle.yt.redditbot.music;

import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;

import javax.validation.constraints.Min;

import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Data;
import lombok.SneakyThrows;

@ConfigurationProperties(prefix = "music")
@Data
@Validated
class MusicSettings {

  private final String folder;
  private final Resource font;
  private final long cardAnimateInTime;
  private final long cardIdleTime;
  private final long cardAnimateOutTime;

  @Min(1)
  private final long minTime;

  @Range(min = -70, max = -5, message = "See https://ffmpeg.org/ffmpeg-filters.html#loudnorm")
  private final float normalization;

  public Duration getMinTime() {
    return new Duration(minTime);
  }

  public Duration getCardAnimateInTime() {
    return new Duration(cardAnimateInTime);
  }

  public Duration getCardIdleTime() {
    return new Duration(cardIdleTime);
  }

  public Duration getCardAnimateOutTime() {
    return new Duration(cardAnimateOutTime);
  }

  @SneakyThrows
  public Path getFolder() {
    return Path.of(folder);
  }
}
