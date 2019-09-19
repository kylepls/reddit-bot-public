package in.kyle.yt.redditbot.music;

import in.kyle.yt.redditbot.timeline.TestTimestamp;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestTimedSong {

  public static TimedSong newTimedSong() {
    return TimedSong.builder().song(TestSong.newSong()).start(TestTimestamp.newTimestamp()).build();
  }
}
