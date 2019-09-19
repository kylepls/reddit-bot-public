package in.kyle.yt.redditbot;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

import in.kyle.yt.redditbot.task.RedditGenerateVideoTask;

@SpringBootTest
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class EE_Application {

  @Autowired RedditGenerateVideoTask taskDispatcher;

  @Test
  void testFull() {
    long start = System.currentTimeMillis();
    taskDispatcher.runGeneration();
    long end = System.currentTimeMillis();
    System.out.println(
        "Video generation completed in " + DurationFormatUtils.formatDurationHMS(end - start));
  }

  @Test
  void testResumeLatest() {
    Path taskFile = Path.of("temp", "tasks", "run-Sat Aug 31 17_14_12 MDT 2019.json");
    taskDispatcher.continueGeneration(taskFile);
  }
}
