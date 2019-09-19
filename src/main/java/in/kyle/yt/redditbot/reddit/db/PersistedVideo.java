package in.kyle.yt.redditbot.reddit.db;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.Data;

@Document(collection = "videos")
@Data
public class PersistedVideo {

  private final RedditThread thread;
  private final List<RedditComment> comments;
  private final List<PersistableSong> songs;
  private final List<String> tags;
  private final String title;
  private final String description;
  private String id;
}
