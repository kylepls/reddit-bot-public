package in.kyle.yt.redditbot.task.impl;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.music.TimedSong;
import in.kyle.yt.redditbot.reddit.generator.images.RedditContextTextParts;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.render.RenderDirectory;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Builder(toBuilder = true)
@Value
@Wither
public class VideoGenContext {

  Path rootDirectory;
  RedditThread thread;
  List<RedditComment> comments;
  RedditContextTextParts textParts;
  List<TimedSong> songs;
  RenderDirectory directory;
  List<String> tags;
  String title;
  String description;
  String comment;
    List<String> keywords;
}
