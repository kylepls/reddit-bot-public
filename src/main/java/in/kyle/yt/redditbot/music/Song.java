package in.kyle.yt.redditbot.music;

import java.nio.file.Path;

import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Wither
public class Song {

  String author;
  String name;
  Path file;
  Duration duration;
}
