package in.kyle.yt.redditbot.reddit.db;

import org.springframework.stereotype.Controller;

import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RedditVideoHistory {

  private final RedditVideoRepository repository;

  public boolean isUnique(RedditThread thread) {
    PersistedVideo video = repository.findDistinctFirstByThreadId(thread.getIdentifier());
    return video == null;
  }

  public List<PersistedVideo> getVideos() {
    return repository.findAll();
  }

  public void save(PersistedVideo video) {
    repository.save(video);
  }
}
