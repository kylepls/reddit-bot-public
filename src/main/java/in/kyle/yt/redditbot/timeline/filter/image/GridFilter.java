package in.kyle.yt.redditbot.timeline.filter.image;

import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.filter.model.ImageFilter;
import lombok.Data;

@Data
public class GridFilter implements ImageFilter {

  private String width = "iw/3-t/3";
  private String height = "ih/3-t/3";
  private String thickness = "2";
  private String color = "red";
  private float opacity = 0.5f;

  @Override
  public String getName() {
    return "drawgrid";
  }

  @Override
  public List<Pair<String, String>> getValues() {
    return List.of(
        Pair.of("w", width),
        Pair.of("h", height),
        Pair.of("t", thickness),
        Pair.of("c", String.format("%s@%s", color, opacity)));
  }
}
