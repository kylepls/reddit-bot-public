package in.kyle.yt.redditbot.graphics;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class GraphicsUtils {

  @SneakyThrows
  public static Font loadFont(File file) {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font font = Font.createFont(Font.TRUETYPE_FONT, file);
    ge.registerFont(font);
    return font;
  }
  
  public static void drawString(Graphics g, String text, int x, int y) {
    g.drawString(text, x, y + g.getFontMetrics().getAscent());
  }
  
  public static BufferedImage toEllipse(Image input) {
    int width = input.getWidth(null);
    int height = input.getHeight(null);
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setComposite(AlphaComposite.Clear);
    g.fillRect(0, 0, width, height);
    addRenderHints(g);
    g.setClip(new Ellipse2D.Float(0, 0, width, height));
    g.setComposite(AlphaComposite.DstAtop);
    g.drawImage(input, 0, 0, width, height, null);
    g.dispose();
    return image;
  }
  
  public static void addRenderHints(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                       RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                       RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                       RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g.setRenderingHint(
        RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                       RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
  }
  
  public static void drawOutlinedString(Graphics2D g,
                                        String text,
                                        int x,
                                        int y,
                                        Color outlineColor,
                                        Stroke stroke) {
    addRenderHints(g);
    y += g.getFontMetrics().getAscent();
    Color originalColor = g.getColor();
    Stroke originalStroke = g.getStroke();
    RenderingHints originalHints = g.getRenderingHints();

    g.translate(x, y);
    TextLayout layout = new TextLayout(text, g.getFont(), g.getFontRenderContext());
    Shape shape = layout.getOutline(null);
    
    g.setColor(outlineColor);
    g.setStroke(stroke);
    g.draw(shape);
    
    g.setColor(originalColor);
    g.fill(shape);
    g.translate(-x, -y);
    
    g.setStroke(originalStroke);
    g.setRenderingHints(originalHints);
  }

  public static float getMaxFontSize(Graphics g, String text, int maxWidth) {
    int oldSize = g.getFont().getSize();
    int stringWidth = g.getFontMetrics().stringWidth(text);
    return (maxWidth / (float) stringWidth) * oldSize;
  }

  public static void setFontSize(Graphics g, float newSize) {
    Font font = g.getFont();
    Font newFont = font.deriveFont(font.getStyle(), newSize);
    g.setFont(newFont);
  }
}
