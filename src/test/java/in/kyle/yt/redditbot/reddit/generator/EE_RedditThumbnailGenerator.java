package in.kyle.yt.redditbot.reddit.generator;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;

import in.kyle.yt.redditbot.reddit.generator.thumbnail.RedditThumbnailGenerator;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.model.Subreddit;
import in.kyle.yt.redditbot.reddit.model.TestRedditAward;
import in.kyle.yt.redditbot.reddit.model.TestSubreddit;
import in.kyle.yt.redditbot.utils.Resources;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class EE_RedditThumbnailGenerator {
  
  @Autowired RedditThumbnailGenerator generator;
  
  private Path outputDirectory;
  
  @BeforeEach
  void setup() throws IOException {
    outputDirectory = Resources.getTestDirectory(getClass());
  }
  
  @Test
  void test() {
    for (int i = 0; i < 10; i++) {
      String title = new Faker().gameOfThrones().quote();
      log.info("Title: {}:{}", i, title);
      
      Subreddit subreddit = TestSubreddit.newSubreddit();
      RedditThread thread = RedditThread.builder()
              .subreddit(subreddit)
              .content(title)
              .award(TestRedditAward.SILVER)
              .award(TestRedditAward.GOLD)
              .award(TestRedditAward.PLATINUM)
              .build();
      Path outputFile = outputDirectory.resolve(i + ".png");
      generator.generate(thread, outputFile);
      System.out.println(outputFile);
    }
  }
}
