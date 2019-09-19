package in.kyle.yt.redditbot.reddit.generator.thumbnail;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.graphics.GraphicsUtils;
import in.kyle.yt.redditbot.keyword.KeywordExtractor;
import in.kyle.yt.redditbot.reddit.model.RedditAward;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static in.kyle.yt.redditbot.graphics.GraphicsUtils.drawOutlinedString;
import static in.kyle.yt.redditbot.graphics.GraphicsUtils.getMaxFontSize;

// TODO: 8/17/2019 This needs work
@Service
@RequiredArgsConstructor
public class RedditThumbnailGenerator {

  private final int width = 1280;
  private final int height = 720;

  private final KeywordExtractor keywordExtractor;
  private final RedditThumbnailSettings settings;

  @SneakyThrows
  public void generate(RedditThread thread, Path outputFile) {
    Conditions.isTrue(
        thread.getSubreddit().getDisplayName().length() < 22,
        "Subreddit '{}' too long",
        thread.getTitle());
    // TODO: 8/20/2019 Needs cleanup
    // 9/12/2019 - java.awt needs cleanup
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    Graphics2D g = (Graphics2D) image.getGraphics();
    // background
    BufferedImage bg = ImageIO.read(settings.getBackground().getInputStream());
    g.drawImage(bg, 0, 0, null);

    g.setColor(settings.getFontColor());
  
    GraphicsUtils.addRenderHints(g);
    Font subredditFont = GraphicsUtils.loadFont(settings.getTitleFont().getFile());
    g.setFont(subredditFont);
    GraphicsUtils.setFontSize(g, 76);

    // icon
    Image icon =
        getResizedImage(
            thread.getSubreddit().getIconUrl(), g.getFont().getSize(), g.getFont().getSize());
    icon = GraphicsUtils.toEllipse(icon);
    int xPos = 10;
    int yPos = 5 + (int) Math.ceil(g.getFontMetrics().getDescent() / 2F);
    g.drawImage(icon, xPos, yPos, null);
    xPos += g.getFont().getSize() + 10;

    // subreddit
    drawOutlinedString(g,
                       thread.getSubreddit().getDisplayNamePrefixed(),
                       xPos,
                       8,
                       settings.getOutlineColor(),
                       settings.getSubredditOutline());
    xPos += getStringWidth(g, thread.getSubreddit().getDisplayNamePrefixed()) + 5;

    // awards
    renderAwards(thread, g, xPos);

    // title
    Font titleFont = GraphicsUtils.loadFont(settings.getTitleFont().getFile());
    g.setFont(titleFont);
    drawLines(thread.getTitle(), g, settings.getFontColor());

    g.dispose();
    ImageIO.write(image, "png", Files.newOutputStream(outputFile));
  }

  private void renderAwards(RedditThread thread, Graphics2D g, int xPos) {
    for (RedditAward award : thread.getAwards()) {
      Image awardImage =
          getResizedImage(award.getIconUrl(), g.getFont().getSize(), g.getFont().getSize());
      g.drawImage(
          awardImage, xPos, 5 + (int) Math.ceil(g.getFontMetrics().getDescent() / 2F), null);
      xPos += g.getFont().getSize() + 5;
    }
  }

  private Image getResizedImage(String url, int newWidth, int newHeight) {
    BufferedImage original = getImage(url);
    return original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
  }

  @SneakyThrows
  private BufferedImage getImage(String url) {
    return ImageIO.read(new URL(url));
  }

  private void drawLines(String title, Graphics2D g, Color style) {
    Rectangle2D titleArea = new Rectangle(10, 100, width - 100, height - 150);
    Dimension targetDimension =
            new Dimension((int) titleArea.getWidth(), (int) titleArea.getHeight());
    List<List<String>> lines = makeLines(List.of(title.split(" ")), g, targetDimension);
    GraphicsUtils.setFontSize(g, (float) getFontSize(lines, targetDimension, g));

    setFontSize(g, lines, (int) targetDimension.getWidth());
    float lineSpacing = (float) (targetDimension.getHeight() / lines.size()) + 3F;

    List<String> keywords = keywordExtractor.getKeywords(title);
    for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
      StringBuilder totalLine = new StringBuilder();
      for (String word : lines.get(lineIndex)) {
        if (keywords.contains(word)) {
          g.setColor(Color.ORANGE);
        } else {
          g.setColor(style);
        }
        int x = (int) titleArea.getX();
        int y = (int) (titleArea.getY() + lineSpacing * lineIndex);
        drawPartialString(g, totalLine.toString(), word, x, y);
        totalLine.append(word).append(" ");
      }
    }
  }

  private void setFontSize(Graphics g, List<List<String>> lines, int maxWidth) {
    double fontSize =
        lines.stream()
            .map(l -> String.join(" ", l))
                .mapToDouble(s -> getMaxFontSize(g, s, maxWidth))
                .min()
                .orElseThrow();
    GraphicsUtils.setFontSize(g, (float) fontSize);
  }

  static List<List<String>> makeLines(List<String> parts, Graphics g, Dimension targetDimension) {
    int maxRows = (int) (targetDimension.getHeight() / g.getFontMetrics().getHeight());

    return IntStream.range(1, maxRows)
            .mapToObj(lines -> distribute(parts, lines))
            .max(Comparator.comparingDouble(a -> measureDensity(a, g, targetDimension)))
            .orElseThrow();
  }
  
  private static double measureDensity(List<List<String>> lines,
                                       Graphics graphics,
                                       Dimension targetDimension) {
    Font oldFont = graphics.getFont();
    GraphicsUtils.setFontSize(graphics, (float) getFontSize(lines, targetDimension, graphics));
    double averageXDensity = lines.stream()
            .filter(line -> getStringWidth(graphics, String.join(" ", line)) <
                            targetDimension.getWidth())
            .map(a -> getStringWidth(graphics, String.join(" ", a)) / targetDimension.getWidth())
            .mapToDouble(Double::doubleValue)
            .average()
            .orElseThrow();
    double averageYDensity =
            (lines.size() * graphics.getFontMetrics().getHeight()) / targetDimension.getHeight();
    graphics.setFont(oldFont);
    return averageXDensity * averageYDensity;
  }
  
  private static double getFontSize(List<List<String>> lines,
                                    Dimension targetDimension,
                                    Graphics graphics) {
    double heightBoundedSize = targetDimension.getHeight() / lines.size();
    double widthBoundedSize = targetDimension.getWidth();
    Font oldFont = graphics.getFont();
    GraphicsUtils.setFontSize(graphics, (float) Math.min(heightBoundedSize, widthBoundedSize));
    while (!doesLineFit(lines, graphics, (int) targetDimension.getWidth())) {
      GraphicsUtils.setFontSize(graphics, graphics.getFont().getSize() - 1);
    }

    double size =
            Math.min(Math.min(widthBoundedSize, heightBoundedSize), graphics.getFont().getSize()) -
            2;
    graphics.setFont(oldFont);
    return size;
  }
  
  private static boolean doesLineFit(List<List<String>> lines, Graphics graphics, int width) {
    return lines.stream()
                   .map(l -> String.join(" ", l))
                   .map(l -> graphics.getFontMetrics().stringWidth(l))
                   .mapToInt(Integer::intValue)
                   .max()
                   .orElseThrow() < width;
  }

  private static int getStringWidth(Graphics graphics, String string) {
    return graphics.getFontMetrics().stringWidth(string);
  }
  
  private static List<List<String>> distribute(List<String> parts, int lineCount) {
    List<List<String>> lines = new ArrayList<>();
    
    int charsPerLine = String.join(" ", parts).length() / lineCount;
    
    Iterator<String> iterator = parts.iterator();
    for (int i = 0; i < lineCount; i++) {
      int chars = 0;
      lines.add(new ArrayList<>());
      while (chars < charsPerLine && iterator.hasNext()) {
        String text = iterator.next();
        lines.get(i).add(text);
        chars += text.length() + (chars == 0 ? 0 : 1);
      }
    }

    return lines;
  }
  
  private void drawPartialString(Graphics2D g, String before, String text, int x, int y) {
    int stringWidth = getStringWidth(g, before);
    Color color = settings.getOutlineColor();
    Stroke stroke = settings.getTitleOutline();
    GraphicsUtils.drawOutlinedString(g, text, x + stringWidth, y, color, stroke);
  }
}
