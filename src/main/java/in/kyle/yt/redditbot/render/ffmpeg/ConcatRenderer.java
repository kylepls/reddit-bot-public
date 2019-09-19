package in.kyle.yt.redditbot.render.ffmpeg;

import java.nio.file.Path;
import java.util.List;

public interface ConcatRenderer {
  void concat(List<Path> files, Path renderDir, Path outputFile, long totalDuration);
}
