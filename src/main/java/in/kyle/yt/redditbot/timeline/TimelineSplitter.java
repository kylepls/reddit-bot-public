package in.kyle.yt.redditbot.timeline;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TimelineSplitter {

  public List<Timeline> splitTimeline(Timeline source, Duration interval) {
    List<Timeline> output = new ArrayList<>();
    Timestamp maxTime = source.getMaxTime();

    Timestamp startTime = new Timestamp(0);
    while (startTime.lt(maxTime)) {
      TimeRange window = new TimeRange(startTime, startTime.add(interval).min(maxTime));
      Timeline windowedTimeline = copyTimeline(source, window);
      output.add(windowedTimeline);

      startTime = startTime.add(interval);
    }

    return output;
  }

  private Timeline copyTimeline(Timeline source, TimeRange window) {
    Timeline targetTimeline = new Timeline(source.getDimension(), source.getBackgroundColor());
    for (Track sourceTrack : source.getTracks()) {
      Track targetTrack = targetTimeline.makeTrack("Copy: " + sourceTrack.getName());
      for (TrackElement sourceElement : sourceTrack.getAllElements()) {
        Optional<TimeRange> overlap = sourceElement.getTimeRange().getOverlap(window);
        if (overlap.isPresent()) {
          TimeRange range = overlap.get();
          sourceElement.copyTo(targetTrack, range);
        }
      }
      targetTrack.offsetStartTime(window.getStart());
    }
    return targetTimeline;
  }
}
