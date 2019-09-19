package in.kyle.yt.redditbot.reddit.db;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timestamp;
import lombok.Value;

@Value
public class PersistableSong {

  String author;
  String name;
  Duration duration;
  Timestamp start;
}
