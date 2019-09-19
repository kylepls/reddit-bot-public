package in.kyle.yt.redditbot.timeline.media;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.media.meta.FileMeta;
import in.kyle.yt.redditbot.timeline.media.meta.FileMetaProvider;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoMedia extends VisualMedia {

  private VideoMedia(Path file, String name, Duration duration, Dimension dimension) {
    super(file, List.of(Attributes.IMAGE, Attributes.SOUND), name, duration, dimension);
  }

  @Override
  public VisualMedia copy() {
    return new VideoMedia(getFile(), getName(), getDuration(), getDimension());
  }

  @Component
  @RequiredArgsConstructor
  static class VideoMediaProvider implements MediaProvider<VideoMedia> {

    private final FileMetaProvider metaProvider;

    @Override
    public VideoMedia load(Path file) {
      FileMeta info = metaProvider.getMetadata(file);
      Dimension dimension = info.getDimensions();
      Duration duration = info.getDuration();
      return new VideoMedia(file, file.getFileName().toString(), duration, dimension);
    }

    @Override
    public String[] getExtensions() {
      return new String[] {"mp4"};
    }
  }
}
