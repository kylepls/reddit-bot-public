package in.kyle.yt.redditbot.music;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import in.kyle.yt.redditbot.timeline.Duration;

@SpringBootTest
class EE_MusicCardGenerator {

  @Autowired MusicCardGenerator generator;

  @Autowired MusicLibrary library;

  @Test
  void test() throws IOException {
    TimedSong song = library.makePlaylist(new Duration(1000 * 60 * 2)).get(0);
    BufferedImage image = generator.makeImage(song);
    ImageIO.write(image, "png", new File(getClass().getName() + ".png"));
  }
}
