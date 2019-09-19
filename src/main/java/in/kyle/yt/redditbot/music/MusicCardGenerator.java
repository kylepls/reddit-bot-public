package in.kyle.yt.redditbot.music;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import in.kyle.yt.redditbot.graphics.GraphicsUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
class MusicCardGenerator {

  private final MusicSettings settings;
  private Font font;

  @PostConstruct
  public void loadFont() throws IOException {
    font = GraphicsUtils.loadFont(settings.getFont().getFile()).deriveFont(Font.BOLD, 37);
  }

  @SneakyThrows
  void makeImage(TimedSong song, Path output) {
    BufferedImage image = makeImage(song);
    ImageIO.write(image, "png", Files.newOutputStream(output));
  }

  BufferedImage makeImage(TimedSong song) {
    BufferedImage image = new BufferedImage(500, 130, BufferedImage.TYPE_3BYTE_BGR);
    Graphics2D g = (Graphics2D) image.getGraphics();
    GraphicsUtils.addRenderHints(g);

    g.setColor(Color.decode("0x383838"));
    g.fillRect(0, 0, image.getWidth(), image.getHeight());

    g.setColor(Color.WHITE);
    g.setFont(font);
    String titleString = song.getSong().getAuthor();
    int textHeightOffset = 30;
    g.drawString(titleString, 45, textHeightOffset + g.getFontMetrics().getHeight() / 2);
    g.drawString(
        song.getSong().getName(),
        45,
        (int) (textHeightOffset + g.getFontMetrics().getHeight() * 1.5));
    g.dispose();
    return image;
  }
}
