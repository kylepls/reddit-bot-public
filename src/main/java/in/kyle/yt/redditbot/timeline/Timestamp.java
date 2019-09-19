package in.kyle.yt.redditbot.timeline;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.data.annotation.PersistenceConstructor;

public class Timestamp extends Duration {

  public static Timestamp ZERO = new Timestamp(0);
  public static Timestamp MAX = new Timestamp(Long.MAX_VALUE);

  @PersistenceConstructor
  public Timestamp(long millis) {
    super(millis);
  }

  public Timestamp(Duration duration) {
    super(duration.getMillis());
  }

  public Timestamp(String time) {
    super(time);
  }

  public Timestamp add(long amount) {
    return new Timestamp(getMillis() + amount);
  }

  public Timestamp add(Duration amount) {
    return new Timestamp(getMillis() + amount.getMillis());
  }

  public Timestamp subtract(Duration duration) {
    return new Timestamp(getMillis() - duration.getMillis());
  }

  public boolean isBefore(Timestamp timestamp) {
    return getMillis() < timestamp.getMillis();
  }

  public boolean isBetween(Timestamp lowerBound, Timestamp upperBound) {
    return lowerBound.getMillis() <= getMillis() && getMillis() < upperBound.getMillis();
  }

  public boolean isAfter(Timestamp timestamp) {
    return getMillis() > timestamp.getMillis();
  }

  public Timestamp max(Timestamp other) {
    return new Timestamp(Math.max(getMillis(), other.getMillis()));
  }

  public Timestamp min(Timestamp other) {
    return new Timestamp(Math.min(getMillis(), other.getMillis()));
  }

  public boolean isSame(Timestamp timestamp) {
    return getMillis() == timestamp.getMillis();
  }

  public String formatted() {
    return DurationFormatUtils.formatDurationHMS(getMillis());
  }

  public String toString() {
    return "TS[" + formatted() + "]";
  }
}
