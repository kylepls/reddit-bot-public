package in.kyle.yt.redditbot.timeline;

import org.springframework.stereotype.Component;

@Component
public class TimelinePrinter {
    public String printTimeline(Timeline timeline) {
        StringBuilder sb = new StringBuilder();
        Timestamp maxTime = timeline.getMaxTime();
        Dimension dimension = timeline.getDimension();
        sb.append("Timeline: (")
                .append(maxTime.formatted())
                .append(") ")
                .append(dimension.toSimpleString());
        
        for (Track track : timeline.getTracks()) {
            sb.append("\nTrack: ").append(track.getName()).append(" - ");
            for (var entry : track.getTimeRanges().entrySet()) {
                if (entry.getValue() != null) {
                    sb.append("\n  ")
                            .append(entry.getKey())
                            .append(" -- ")
                            .append(entry.getValue().getMedia().getFile());
                }
            }
        }
        
        return sb.toString();
    }
}
