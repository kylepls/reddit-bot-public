package in.kyle.yt.redditbot.reddit.db;

import in.kyle.yt.redditbot.music.Song;
import in.kyle.yt.redditbot.music.TestSong;
import in.kyle.yt.redditbot.timeline.Timestamp;

public class TestPersistableSong {

  public static PersistableSong newSong() {
    Song song = TestSong.newSong();
    return new PersistableSong(
        song.getAuthor(), song.getName(), song.getDuration(), new Timestamp(0));
  }
}
