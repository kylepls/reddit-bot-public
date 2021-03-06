package in.kyle.yt.redditbot.timeline.media;

import java.nio.file.Path;

import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.media.meta.FileMeta;

public class TestVideoMedia {

  public static Media newVideo(Path path) {
    return new VideoMedia.VideoMediaProvider(
            file ->
                new FileMeta() {
                  @Override
                  public Duration getDuration() {
                    return new Duration(1);
                  }

                  @Override
                  public Dimension getDimensions() {
                    return new Dimension(1920, 1080);
                  }
                })
        .load(path);
  }
}
