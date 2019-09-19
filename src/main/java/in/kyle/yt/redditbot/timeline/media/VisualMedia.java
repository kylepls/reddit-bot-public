package in.kyle.yt.redditbot.timeline.media;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class VisualMedia extends Media {

  private Dimension dimension;

  VisualMedia(
      Path file, List<Attributes> attributes, String name, Duration duration, Dimension dimension) {
    super(file, attributes, name, duration);
    this.dimension = dimension;
  }
}
