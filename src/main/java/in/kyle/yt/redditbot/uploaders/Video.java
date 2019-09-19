package in.kyle.yt.redditbot.uploaders;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class Video {
  
  String title;
  InputStream videoStream;
  long videoLength;
  Path thumbnailFile;
  String description;
  String comment;
  @Singular List<String> tags;
  @Singular
  List<String> keywords;
}
