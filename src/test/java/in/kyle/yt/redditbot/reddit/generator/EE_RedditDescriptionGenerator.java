package in.kyle.yt.redditbot.reddit.generator;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import in.kyle.yt.redditbot.music.TestTimedSong;
import in.kyle.yt.redditbot.reddit.generator.meta.RedditVideoMetaGenerator;
import in.kyle.yt.redditbot.reddit.model.TestRedditThread;
import in.kyle.yt.redditbot.utils.Make;

@SpringBootTest
class EE_RedditDescriptionGenerator {

  @Autowired RedditVideoMetaGenerator generator;

  @Test
  void test() {
    Faker faker = new Faker();
    var thread = TestRedditThread.newRedditThread();
    var timedSongs = Make.make(4, TestTimedSong::newTimedSong);
    var tags = Make.make(10, () -> faker.esports().game());

    String description = generator.generateDescription(thread, timedSongs, tags);
    System.out.println(description);
  }
}
