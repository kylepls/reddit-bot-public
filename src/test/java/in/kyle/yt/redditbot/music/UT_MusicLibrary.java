package in.kyle.yt.redditbot.music;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.TimeRange;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.timeline.media.meta.FileMetaProvider;

import static org.assertj.core.api.Assertions.assertThat;

class UT_MusicLibrary {

  private MusicLibrary musicLibrary;

  @BeforeEach
  void setup() {
    MusicSettings settings = new MusicSettings(null, null, 1, 1, 1, 1000, 1);
    FileMetaProvider metaProvider = file -> null;
    MediaFactory factory = new MediaFactory(List.of(), null);

    Song song1 = new Song("song1 author", "song1", null, new Duration(5000));
    Song song2 = new Song("song2 author", "song2", null, new Duration(5000));
    Song song3 = new Song("song3 author", "song3", null, new Duration(5000));
    Song song4 = new Song("song4 author", "song4", null, new Duration(5000));
    musicLibrary = new MusicLibrary(settings, metaProvider, factory);
    musicLibrary.setSongs(List.of(song1, song2, song3, song4));
  }

  @Test
  void testPlaylistSingle() {
    List<TimedSong> songs = musicLibrary.makePlaylist(new Duration(1000));
    assertThat(songs).hasSize(1);
    TimedSong song = songs.get(0);
    assertThat(song.getStart()).isEqualTo(Timestamp.ZERO);
    assertThat(song.getSong().getDuration()).isEqualTo(new Duration(1000));
  }

  @Test
  void testPlaylistMany() {
    List<TimedSong> songs = musicLibrary.makePlaylist(new Duration(15000));
    assertThat(songs).hasSize(3);
    for (int i = 0; i < 3; i++) {
      TimedSong song = songs.get(i);
      assertThat(song.getStart()).isEqualTo(new Timestamp(5001 * i));
    }
    for (int i = 1; i < 3; i++) {
      TimeRange current = songs.get(i).getTimeRange();
      TimeRange last = songs.get(i - 1).getTimeRange();
      Optional<TimeRange> overlap = current.getOverlap(last);
        assertThat(overlap).as("Timestamps %s %s should not overlap", last, current)
                .isNotPresent()
                .isNotEqualTo(last.getEnd());
    }
      Timestamp end = songs.get(songs.size() - 1).getEnd();
      assertThat(end).isEqualTo(new Duration(15000));
  }

  @Test
  void testMinTimeCut() {
    List<TimedSong> timedSongs = musicLibrary.makePlaylist(new Duration(999));
    assertThat(timedSongs).isEmpty();
  }

  @Test
  void testTrimSongNone() {
    Song song = new Song("test", "test", null, new Duration(1000));
    Timestamp start = new Timestamp(0);
    Timestamp end = new Timestamp(5000);
    Song trimSong = musicLibrary.trimSong(start, song, end);

    assertThat(trimSong.getDuration()).isEqualTo(new Duration(1000));
  }

  @Test
  void testTrimSongCut() {
    Song song = new Song("test", "test", null, new Duration(10000));
    Timestamp start = new Timestamp(0);
    Timestamp end = new Timestamp(5000);
    Song trimSong = musicLibrary.trimSong(start, song, end);

    assertThat(trimSong.getDuration()).isEqualTo(new Duration(5000));
  }
}
