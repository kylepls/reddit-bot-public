package in.kyle.yt.redditbot.timeline.render;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.TrackElement;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;

@Component
public class TimelineVisualizer {

  private static final int WIDTH = 1000;
  private static final int TRACK_HEIGHT = 75;
  private static final int GRID_TITLE_HEIGHT = 20;

  public BufferedImage renderTimeline(Timeline timeline) {
    List<Track> tracks = timeline.getTracks();
    BufferedImage image =
        new BufferedImage(
            WIDTH, GRID_TITLE_HEIGHT + TRACK_HEIGHT * tracks.size(), BufferedImage.TYPE_3BYTE_BGR);
    Graphics g = image.getGraphics();

    Duration timelineDuration = timeline.getMaxTime();
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());

    drawElements(tracks, g, timelineDuration);
    drawGrid(image, g, timelineDuration);
  
    g.dispose();

    return image;
  }

  private void drawElements(List<Track> tracks, Graphics g, Duration timelineDuration) {
    for (int i = 0; i < tracks.size(); i++) {
      // draw division
      g.setColor(Color.BLACK);
      int yStartPos = i * TRACK_HEIGHT + GRID_TITLE_HEIGHT;
      int yEndPos = yStartPos + TRACK_HEIGHT;

      Track track = tracks.get(i);
      for (TrackElement element : track.getAllElements()) {
        drawElement(g, timelineDuration, yStartPos, element);
      }
    }
  }

  private void drawGrid(BufferedImage image, Graphics g, Duration timelineDuration) {
    // draw grid
    g.setColor(Color.BLACK);
    g.drawLine(0, GRID_TITLE_HEIGHT, WIDTH, GRID_TITLE_HEIGHT);
    int divisions = 10;
    long increment = timelineDuration.getMillis() / divisions;
    for (int i = 0; i < divisions; i++) {
      Timestamp ts = new Timestamp(increment * i);
      int x = getFractionalPosition(ts, timelineDuration);
      g.drawLine(x, GRID_TITLE_HEIGHT, x, image.getHeight());
      int stringWidth = g.getFontMetrics().stringWidth(ts.formatted());
      g.drawString(ts.formatted(), x - stringWidth / 2, g.getFontMetrics().getHeight());
    }
  }

  private void drawElement(
      Graphics g, Duration timelineDuration, int yStartPos, TrackElement element) {
    int startPosX = getFractionalPosition(element.getStart(), timelineDuration);
    int endPosX = getFractionalPosition(element.getEnd(), timelineDuration);
    Color color;
    if (element instanceof VisualTrackElement) {
      color = Color.BLUE;
    } else {
      color = Color.GREEN;
    }
    g.setColor(color);
    g.fillRect(startPosX, yStartPos, endPosX - startPosX, TRACK_HEIGHT);

    g.setColor(Color.DARK_GRAY);
    g.drawRect(startPosX, yStartPos, endPosX - startPosX, TRACK_HEIGHT);
    g.setColor(Color.BLACK);
    FontMetrics fm = g.getFontMetrics();
    g.drawString(element.getMedia().getName(), startPosX, yStartPos + fm.getHeight());
  }

  private int getFractionalPosition(Timestamp position, Duration total) {
    return (int) ((position.getMillis() / (double) total.getMillis()) * WIDTH);
  }
}
