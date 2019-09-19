package in.kyle.yt.redditbot.music;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.timeline.media.SoundMedia;
import in.kyle.yt.redditbot.timeline.media.meta.FileMetaProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class MusicLibrary {

  private final List<Song> songs = new CopyOnWriteArrayList<>();
  private final MusicSettings settings;
  private final FileMetaProvider metaProvider;
  private final MediaFactory mediaFactory;

  @PostConstruct
  public void loadSongs() throws IOException {
    if (Files.exists(settings.getFolder())) {
      Files.list(settings.getFolder()).parallel().map(this::loadSong).forEach(songs::add);
    } else {
      log.info("No songs folder exists, generating...");
      Files.createDirectories(settings.getFolder());
    }
  }

  void setSongs(List<Song> songs) {
    this.songs.clear();
    this.songs.addAll(songs);
  }

  List<TimedSong> makePlaylist(Duration duration) {
    Duration maxPlaylistDuration = getMaxPlaylistDuration();
    Conditions.isTrue(
        maxPlaylistDuration.gt(duration),
        "Not enough songs for {} playlist, have {}",
        duration,
        maxPlaylistDuration);
    Collections.shuffle(songs);
    List<TimedSong> result = new ArrayList<>();

    Iterator<Song> shuffledSongs = songs.iterator();
    Timestamp nextSongStart = new Timestamp(0);
    while (nextSongStart.lt(duration)) {
      Timestamp songStart = nextSongStart;
      Song song = trimSong(nextSongStart, shuffledSongs.next(), new Timestamp(duration));

      if (song.getDuration().gte(settings.getMinTime())) {
        result.add(new TimedSong(songStart, song.withDuration(song.getDuration())));
      }
  
      nextSongStart = nextSongStart.add(song.getDuration()).add(1);
    }
    return List.copyOf(result);
  }

  Song trimSong(Timestamp songstart, Song song, Timestamp maxTime) {
    Timestamp songEnd = songstart.add(song.getDuration());
    if (songEnd.gt(maxTime)) {
      Duration delta = songstart.add(song.getDuration()).difference(maxTime);
      Duration newSongDuration = song.getDuration().subtract(delta);
      return song.withDuration(newSongDuration);
    } else {
      return song;
    }
  }

  private Duration getMaxPlaylistDuration() {
    return new Duration(songs.stream().mapToLong(s -> s.getDuration().getMillis()).sum());
  }

  private Song loadSong(Path file) {
    log.info("Loading song {}", file);
    String fileName = file.getFileName().toString();
    String author = fileName.substring(0, fileName.indexOf("__"));
    String name = fileName.substring(fileName.indexOf("__") + 2, fileName.lastIndexOf("."));
    Duration duration = metaProvider.getMetadata(file).getDuration();
    return new Song(author, name, file, duration);
  }

  SoundMedia loadMedia(Song song) {
    return mediaFactory.load(song.getFile());
  }
}
