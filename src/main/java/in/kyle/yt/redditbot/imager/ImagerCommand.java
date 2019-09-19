package in.kyle.yt.redditbot.imager;

import java.nio.file.Path;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ImagerCommand {

  String url;
  String payload;
  String parentCssSelector;
  String textCssSelector;
  boolean incremental;
  Path outputFilePrefix;
}
