package in.kyle.yt.redditbot.timeline;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@RequiredArgsConstructor
public class Timeline {

  private final List<Track> tracks = new ArrayList<>();

  @Getter private final Dimension dimension;
  @Getter private final Color backgroundColor;

  @Synchronized
  public Track makeTopTrack(String name) {
    Track track = new Track(name, dimension);
    tracks.add(0, track);
    return track;
  }

  @Synchronized
  public Track makeTrack(String name) {
    Track track = new Track(name, dimension);
    tracks.add(track);
    return track;
  }

  public List<Track> getTracks() {
    return List.copyOf(tracks);
  }

  public Timestamp getMaxTime() {
    return getTracks().stream()
        .map(Track::getEnd)
        .max(Comparator.comparingLong(Duration::getMillis))
        .orElseThrow();
  }
}
