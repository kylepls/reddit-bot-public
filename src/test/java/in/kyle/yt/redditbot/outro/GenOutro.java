package in.kyle.yt.redditbot.outro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import in.kyle.yt.redditbot.graphics.GraphicsUtils;
import in.kyle.yt.redditbot.reddit.generator.video.RedditVideoSettings;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.render.TestRenderDirectory;
import in.kyle.yt.redditbot.render.TimelineRenderer;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import in.kyle.yt.redditbot.timeline.media.Media;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.tts.TtsEngine;
import lombok.SneakyThrows;

import static in.kyle.yt.redditbot.graphics.GraphicsUtils.drawString;
import static in.kyle.yt.redditbot.graphics.GraphicsUtils.getMaxFontSize;
import static in.kyle.yt.redditbot.graphics.GraphicsUtils.loadFont;
import static in.kyle.yt.redditbot.graphics.GraphicsUtils.setFontSize;

@SpringBootTest
class GenOutro {

  private static final int WIDTH = 1600;

  @Autowired TtsEngine ttsEngine;
  @Autowired TimelineRenderer renderer;
  @Autowired RedditVideoSettings settings;
  @Autowired MediaFactory mediaFactory;

  private RenderDirectory directory;

  @BeforeEach
  void setup() {

    directory = TestRenderDirectory.newDirectory(getClass());
  }

  @Test
  void test() {
    String input = "Thanks for watching. Don't forget to like and subscribe for more!";
    Timeline timeline = new Timeline(new Dimension(1920, 1080), settings.getBackgroundColor());
    generateIncremental(input, timeline);
    renderer.render(timeline, directory.getOutputFile());
  }

  private void generateIncremental(String text, Timeline timeline) {
    Track images = timeline.makeTrack("Images");
    Track sounds = timeline.makeTrack("Sounds");

    String partialText = "";
    String[] parts = text.split("\\.");
    for (int i = 0; i < parts.length; i++) {
      String part = parts[i];
      partialText += (i != 0 ? "." : "") + part;

      Path ttsFile = directory.getSounds().resolve(String.format("%d.mp3", i));
      generateTts(part, ttsFile);
      Media sound = mediaFactory.load(ttsFile);
      sounds.put(sounds.getEnd().add(1), sound);

      Path imageFile = directory.getImages().resolve(String.format("%d.png", i));
      generatePartialImage(text, partialText, imageFile);
      Media image = mediaFactory.load(imageFile);
      image.setDuration(sound.getDuration());
      VisualTrackElement element = images.put(images.getEnd().add(1), image);
      element.setPosition("W/2-w/2", "H/2-h/2", true);
    }
  }

  private void generateTts(String part, Path ttsFile) {
    ttsEngine.generateSpeech(part, ttsFile, 1);
  }

  @SneakyThrows
  private void generatePartialImage(String fullText, String imageText, Path outputFile) {
    BufferedImage image = new BufferedImage(WIDTH, WIDTH, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D graphics = (Graphics2D) image.getGraphics();
    GraphicsUtils.addRenderHints(graphics);

    graphics.setColor(settings.getBackgroundColor());
    graphics.fillRect(0, 0, WIDTH, WIDTH);

    Font font = loadFont(new File("fonts/Candara.ttf"));
    graphics.setFont(font);
    setFontSize(graphics, getMaxFontSize(graphics, fullText, WIDTH));
    graphics.setColor(Color.WHITE);
    drawString(graphics, imageText, 0, 0);

    BufferedImage subimage = image.getSubimage(0, 0, WIDTH, graphics.getFontMetrics().getHeight());
    ImageIO.write(subimage, "png", Files.newOutputStream(outputFile));
  }
}
