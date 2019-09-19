package in.kyle.yt.redditbot.timeline;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.timeline.media.Media;
import in.kyle.yt.redditbot.timeline.media.VisualMedia;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Track {

  private final LinkedList<TrackElement> media = new LinkedList<>();

  @Getter private final String name;
  @Getter private final Dimension dimension;

  public List<TrackElement> getAllElements() {
    return List.copyOf(media);
  }

  public void offsetStartTime(Timestamp time) {
    for (TrackElement element : media) {
      element.setStart(element.getStart().subtract(time));
    }
  }

  public LinkedHashMap<TimeRange, TrackElement> getTimeRanges() {
    LinkedHashMap<TimeRange, TrackElement> timeRanges = new LinkedHashMap<>();
    Timestamp last = Timestamp.ZERO;

    for (TrackElement element : media) {
      Timestamp start = element.getStart();
      Timestamp end = element.getEnd();
      if (last.difference(start).gt(new Duration(1))) { // Jump
        timeRanges.put(new TimeRange(last, start.add(-1)), null);
      }
      timeRanges.put(new TimeRange(start, end), element);
      last = end.add(1);
    }

    timeRanges.put(new TimeRange(last, new Timestamp(Long.MAX_VALUE)), null);
    return timeRanges;
  }

  private int listIndexOf(Timestamp timestamp) {
    int i = 0;
    for (; i < media.size(); i++) {
      TrackElement e = media.get(i);
      if (e.getStart().isAfter(timestamp)) {
        return i;
      }
    }
    return i;
  }

  public TrackElement get(Timestamp timestamp) {
    var ranges = getTimeRanges();
    var entry =
        ranges.entrySet().stream()
            .filter(e -> e.getKey().contains(timestamp))
            .findFirst()
            .orElseThrow();
    return entry.getValue();
  }

  private Map.Entry<TimeRange, TrackElement> getTimeRange(Timestamp timestamp) {
    return getTimeRanges().entrySet().stream()
        .filter(r -> r.getKey().contains(timestamp))
        .findFirst()
        .orElseThrow();
  }

  @Synchronized
  public <T extends TrackElement> T put(Timestamp start, Media media) {
    var existingEntry = getTimeRange(start);
    TimeRange existingRange = existingEntry.getKey();
    TimeRange newRange = new TimeRange(start, start.add(media.getDuration()));

    Conditions.isTrue(
        existingEntry.getValue() == null && existingRange.contains(newRange),
        "Timestamp overlap\nExisting : {}\nNew      : {}\n{}",
        existingRange,
        newRange,
        this);

    TrackElement element = makeElement(start, media);
    int index = listIndexOf(element.getStart());
    this.media.add(index, element);
    return (T) element;
  }

  private TrackElement makeElement(Timestamp start, Media media) {
    TrackElement element;
    if (media instanceof VisualMedia) {
      element = new VisualTrackElement(start, media);
    } else {
        element = new TrackElement(media, start);
    }
    return element;
  }

  public Timestamp getEnd() {
    if (media.isEmpty()) {
      return new Timestamp(0);
    } else {
      return media.getLast().getEnd();
    }
  }

  @Override
  public String toString() {
    return "Track[" + name + "]";
  }
}
