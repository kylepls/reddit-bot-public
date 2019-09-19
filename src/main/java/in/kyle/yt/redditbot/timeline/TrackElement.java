package in.kyle.yt.redditbot.timeline;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import in.kyle.yt.redditbot.timeline.filter.model.AudioFilter;
import in.kyle.yt.redditbot.timeline.filter.model.Filter;
import in.kyle.yt.redditbot.timeline.filter.model.ImageFilter;
import in.kyle.yt.redditbot.timeline.media.Media;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrackElement {

  private final LinkedHashSet<Filter> filters = new LinkedHashSet<>();
  private final Media media;
  private Timestamp start;

  public void setFilters(Collection<Filter> filters) {
    this.filters.addAll(filters);
  }

  public Timestamp getEnd() {
    return new Timestamp(start.getMillis() + media.getDuration().getMillis());
  }

  public Duration getDuration() {
    return getEnd().difference(getStart());
  }

  public void addFilter(Filter filter) {
    filters.add(filter);
  }

  public List<Filter> getAudioFilters() {
    return filters.stream().filter(e -> e instanceof AudioFilter).collect(Collectors.toList());
  }

  public List<Filter> getVideoFilters() {
    return filters.stream().filter(e -> e instanceof ImageFilter).collect(Collectors.toList());
  }

  public TimeRange getTimeRange() {
    return new TimeRange(start, start.add(getDuration()));
  }

  public TrackElement copyTo(Track target, TimeRange timerange) {
    Media media = getMedia().copy();
    media.setDuration(timerange.getDuration());
    media.setStartOffset(getStart().difference(timerange.getStart()));
    TrackElement element = target.put(timerange.getStart(), media);
    element.setFilters(getFilters());
    return element;
  }

  @Override
  public String toString() {
    return String.format("TrackElement[%s: %s]", getMedia().getName(), getTimeRange());
  }
}
