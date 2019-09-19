package in.kyle.yt.redditbot.imager;

import java.nio.file.Path;
import java.util.List;

import lombok.Value;

@Value
public class ImagerResult {

  List<String> parts;
  List<Path> outputFiles;
}
