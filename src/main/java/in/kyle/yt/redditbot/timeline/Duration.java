package in.kyle.yt.redditbot.timeline;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.data.annotation.PersistenceConstructor;

import in.kyle.api.utils.TimeUtils;
import lombok.Data;

@Data
public class Duration {

  private final long millis;

  @PersistenceConstructor
  public Duration(long millis) {
    this.millis = millis;
  }

  public Duration(String time) {
    this(TimeUtils.getDuration(time));
  }

  public float getSeconds() {
    return millis / 1000F;
  }

  public boolean isZero() {
    return getMillis() == 0;
  }

  public Duration add(Duration duration) {
    return new Duration(millis + duration.getMillis());
  }

  public Duration subtract(Duration duration) {
    return new Duration(millis - duration.getMillis());
  }

  public Duration add(long millis) {
    return new Duration(this.millis + millis);
  }

  public Duration subtract(long millis) {
    return new Duration(this.millis - millis);
  }

  public boolean lt(Duration duration) {
    return millis < duration.getMillis();
  }

  public boolean gt(Duration duration) {
    return millis > duration.getMillis();
  }

  public boolean gte(Duration duration) {
    return millis >= duration.getMillis();
  }

  public boolean lte(Duration duration) {
    return millis <= duration.getMillis();
  }

  public Duration difference(Duration d1) {
    return new Duration(Math.abs(d1.getMillis() - getMillis()));
  }

  public float getFractionalSeconds() {
    return getMillis() / 1000F;
  }

  public String toString() {
    return "Duration[" + DurationFormatUtils.formatDurationHMS(getMillis()) + "]";
  }

  public boolean eq(Duration duration) {
    return getMillis() == duration.getMillis();
  }
}
