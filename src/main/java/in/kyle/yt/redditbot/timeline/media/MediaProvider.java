package in.kyle.yt.redditbot.timeline.media;

import java.nio.file.Path;

public interface MediaProvider<T extends Media> {

  T load(Path file);

  String[] getExtensions();
}
