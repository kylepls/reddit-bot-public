package in.kyle.yt.redditbot.timeline.media;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.media.meta.FileMeta;
import in.kyle.yt.redditbot.timeline.media.meta.FileMetaProvider;
import lombok.RequiredArgsConstructor;

public class SoundMedia extends Media {

  private SoundMedia(Path file, String name, Duration duration) {
    super(file, List.of(Attributes.SOUND), name, duration);
  }

  @Override
  public List<Attributes> getAttributes() {
    return List.of(Attributes.SOUND);
  }

  @Override
  public SoundMedia copy() {
    return new SoundMedia(getFile(), getName(), getDuration());
  }

  @Component
  @RequiredArgsConstructor
  static class SoundMediaProvider implements MediaProvider<SoundMedia> {

    private final FileMetaProvider metaProvider;

    @Override
    public SoundMedia load(Path file) {
      FileMeta info = metaProvider.getMetadata(file);
      Duration duration = info.getDuration();
      return new SoundMedia(file, file.getFileName().toString(), duration);
    }

    @Override
    public String[] getExtensions() {
      return new String[] {"mp3", "wav"};
    }
  }
}
