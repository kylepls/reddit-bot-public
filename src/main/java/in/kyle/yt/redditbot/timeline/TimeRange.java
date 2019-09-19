package in.kyle.yt.redditbot.timeline;

import java.util.Optional;

import in.kyle.api.utils.Conditions;
import lombok.Value;

@Value
public class TimeRange {

  Timestamp start;
  Timestamp end;

  public TimeRange(Timestamp start, Timestamp end) {
    this.start = start;
    this.end = end;
    Conditions.isTrue(start.lte(end), "Invalid time range {}", this);
  }

  public Optional<TimeRange> getOverlap(TimeRange other) {
    if (end.isBefore(other.getStart()) || start.isAfter(other.getEnd())) {
      return Optional.empty();
    } else {
      return Optional.of(new TimeRange(start.max(other.start), end.min(other.end)));
    }
  }

  public boolean contains(Timestamp timestamp) {
    return start.lte(timestamp) && end.gte(timestamp);
  }

  public boolean contains(TimeRange timeRange) {
    return contains(timeRange.start) && contains(timeRange.end);
  }

  public Duration getDuration() {
    return end.subtract(start);
  }

  @Override
  public String toString() {
    return String.format("TR[%s -- %s]", start.formatted(), end.formatted());
  }
}
