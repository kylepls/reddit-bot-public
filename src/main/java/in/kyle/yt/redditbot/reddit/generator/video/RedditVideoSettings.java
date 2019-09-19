package in.kyle.yt.redditbot.reddit.generator.video;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;

import java.awt.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;

import in.kyle.api.utils.TimeUtils;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "reddit.video")
@Data
@Validated
public class RedditVideoSettings {

  @Min(1)
  private final int dimensionWidth;

  @Min(1)
  private final int dimensionHeight;

  private final String backgroundColor;
  private final Resource intro;
  private final Resource outro;
  private final Resource transition;

  @Min(0)
  private final int minComments;

  @Min(0)
  private final int maxComments;

  private final String minTime;
  private final String targetTime;

  @PostConstruct
  void log() {
    log.info("Intro: {}", intro);
    log.info("Outro: {}", outro);
    log.info("Transition: {}", transition);
  }

  public Dimension getDimension() {
    return new Dimension(dimensionWidth, dimensionHeight);
  }

  public Color getBackgroundColor() {
    return Color.decode(backgroundColor);
  }

  public Duration getMinTime() {
    return new Duration(TimeUtils.getDuration(minTime));
  }

  public Duration getTargetTime() {
    return new Duration(TimeUtils.getDuration(targetTime));
  }
}
