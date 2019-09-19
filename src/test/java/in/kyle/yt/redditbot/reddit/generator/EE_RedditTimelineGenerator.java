package in.kyle.yt.redditbot.reddit.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import in.kyle.yt.redditbot.reddit.generator.video.RedditTimelineGenerator;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.render.TestRenderDirectory;
import in.kyle.yt.redditbot.render.TimelineRenderer;
import in.kyle.yt.redditbot.timeline.Timeline;

@SpringBootTest
class EE_RedditTimelineGenerator {

  @Autowired RedditTimelineGenerator videoGenerator;
  @Autowired TimelineRenderer renderer;
  private RenderDirectory directory;

  @BeforeEach
  void setup() {
    directory = TestRenderDirectory.newDirectoryWithResources(getClass());
  }

  @Test
  void test() {
    List<RedditComment> comments = TestRenderDirectory.getComments(directory);

    Timeline timeline = videoGenerator.generateTimeline(directory, comments);
    renderer.render(timeline, directory.getOutputFile());
    System.out.println(directory.getOutputFile());
  }
}
