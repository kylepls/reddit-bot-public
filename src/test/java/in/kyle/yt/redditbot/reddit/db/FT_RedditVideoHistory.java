package in.kyle.yt.redditbot.reddit.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.utils.Make;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FT_RedditVideoHistory {

  @Autowired RedditVideoHistory history;

  @Test
  void test() {
    Make.make(5, TestPersistedVideo::newPersistedVideo).forEach(history::save);
    PersistedVideo video = TestPersistedVideo.newPersistedVideo();
    history.save(video);

    List<PersistedVideo> videos = history.getVideos();
    assertThat(videos.size()).isEqualTo(6);

    assertThat(history.isUnique(thread("new-id"))).isTrue();
    assertThat(history.isUnique(thread(video.getThread().getIdentifier()))).isFalse();
  }

  private RedditThread thread(String id) {
    return RedditThread.builder().identifier(id).build();
  }
}
