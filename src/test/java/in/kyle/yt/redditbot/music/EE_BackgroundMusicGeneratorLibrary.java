package in.kyle.yt.redditbot.music;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.awt.*;
import java.nio.file.Path;

import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.render.TestRenderDirectory;
import in.kyle.yt.redditbot.render.TimelineRenderer;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.media.Media;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;

@SpringBootTest
@TestPropertySource(
    properties = {
      "music.folder=target/tests/in.kyle.yt.redditbot.music"
          + ".EE_BackgroundMusicGeneratorLibrary/music",
      "music.min_time=5000",
      "music.cardAnimateInTime=1000",
      "music.cardIdleTime=1000",
      "music.cardAnimateOutTime=1000"
    })
class EE_BackgroundMusicGeneratorLibrary {

  private static RenderDirectory directory;

  @Autowired BackgroundMusicGenerator backgroundMusic;
  @Autowired MediaFactory mediaFactory;
  @Autowired TimelineRenderer renderer;

  @BeforeAll
  static void setup() {
    directory =
        TestRenderDirectory.newDirectoryWithResources(EE_BackgroundMusicGeneratorLibrary.class);
    System.out.println();
  }

  @Test
  void test() {
    Timeline timeline = new Timeline(new Dimension(1920, 1080), Color.RED);

    Path imagePath = directory.getImages().resolve("dank1.jpg");
    Media imageMedia = mediaFactory.load(imagePath);
    imageMedia.setDuration(new Duration("5s"));

    Track track = timeline.makeTrack("test");
    track.put(new Timestamp(0), imageMedia);

    backgroundMusic.addBackgroundMusic(timeline, directory);

    renderer.render(timeline, directory.getOutputFile());
    System.out.println(directory.getOutputFile());
  }
}
