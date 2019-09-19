package in.kyle.yt.redditbot.render;

import java.nio.file.Path;

import in.kyle.yt.redditbot.timeline.Timeline;

public interface TimelineRenderer {

  void render(Timeline timeline, Path outputFile);
}
