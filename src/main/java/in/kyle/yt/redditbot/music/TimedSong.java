package in.kyle.yt.redditbot.music;

import in.kyle.yt.redditbot.timeline.TimeRange;
import in.kyle.yt.redditbot.timeline.Timestamp;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TimedSong {

  Timestamp start;
  Song song;
  
  public Timestamp getEnd() {
    return start.add(song.getDuration());
  }
  
  public TimeRange getTimeRange() {
    return new TimeRange(start, getEnd());
  }
}
