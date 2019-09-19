package in.kyle.yt.redditbot.timeline.media.meta;

import java.nio.file.Path;

public interface FileMetaProvider {

  FileMeta getMetadata(Path file);
}
