package in.kyle.yt.redditbot.reddit.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Subreddit {

  String name;
  String displayName;
  String displayNamePrefixed;
  String iconUrl;
}
