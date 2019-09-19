package in.kyle.yt.redditbot.timeline.media;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Duration;
import lombok.Data;

@Data
public abstract class Media {

  private final Path file;
  private final List<Attributes> attributes;
  private String name;
  private Duration duration;
  private Duration startOffset;

  public Media(Path file, List<Attributes> attributes, String name, Duration duration) {
    this.file = file;
    this.attributes = attributes;
    this.name = name;
    this.duration = duration;
    this.startOffset = new Duration(0);
  }

  public boolean hasAttributes(Attributes... attributes) {
    return getAttributes().containsAll(List.of(attributes));
  }

  public abstract Media copy();

  public enum Attributes {
    SOUND,
    IMAGE
  }
}
