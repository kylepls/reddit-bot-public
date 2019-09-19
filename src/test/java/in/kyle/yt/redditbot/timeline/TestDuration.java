package in.kyle.yt.redditbot.timeline;

import java.util.concurrent.TimeUnit;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDuration {

  public static Duration newDuration() {
    return new Duration((long) (TimeUnit.HOURS.toMillis(1) * Math.random()));
  }
}
