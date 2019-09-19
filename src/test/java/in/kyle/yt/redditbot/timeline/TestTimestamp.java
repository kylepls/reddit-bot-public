package in.kyle.yt.redditbot.timeline;

import java.util.concurrent.TimeUnit;

public class TestTimestamp {

  public static Timestamp newTimestamp() {
    return new Timestamp((long) (TimeUnit.HOURS.toMillis(1) * Math.random()));
  }
}
