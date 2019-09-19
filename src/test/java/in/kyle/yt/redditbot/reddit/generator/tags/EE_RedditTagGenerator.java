package in.kyle.yt.redditbot.reddit.generator.tags;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditThread;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EE_RedditTagGenerator {
  
  @Autowired
  RedditTagGenerator tagsGenerator;
  
  @Test
  void getTags() {
    // Sanity check to see if API responds
    String title = "What is the saddest thing you've ever seen? (r/AskReddit Top Posts)";
    RedditThread thread = RedditThread.builder().content(title).build();
    List<String> tags = tagsGenerator.getTags(thread);
    assertThat(tags).startsWith("reddit", "askreddit", "stories");
    assertThat(tags).contains("top posts", "reddit top posts", "askreddit top posts");
    assertThat(tags).endsWith("funny stories", "updoot", "toadfilms", "readit");
  }
}
