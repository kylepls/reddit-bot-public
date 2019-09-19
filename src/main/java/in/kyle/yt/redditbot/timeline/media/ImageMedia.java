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
public class ImageMedia extends VisualMedia {

  private ImageMedia(Path file, String name, Duration duration, Dimension dimension) {
    super(file, List.of(Attributes.IMAGE), name, duration, dimension);
  }

  @Override
  public ImageMedia copy() {
    return new ImageMedia(getFile(), getName(), getDuration(), getDimension());
  }

  @Component
  @RequiredArgsConstructor
  static class ImageMediaProvider implements MediaProvider<ImageMedia> {

    private final FileMetaProvider metaProvider;

    @Override
    public ImageMedia load(Path file) {
      FileMeta info = metaProvider.getMetadata(file);
      Dimension dimension = info.getDimensions();
      return new ImageMedia(file, file.getFileName().toString(), new Duration(10), dimension);
    }

    @Override
    public String[] getExtensions() {
      return new String[] {"png", "jpg"};
    }
  }
}
