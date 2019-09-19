package in.kyle.yt.redditbot.timeline.filter.image;

import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.filter.model.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class DimensionFilter implements ImageFilter {

  private String width = "-1";
  private String height = "-1";

  public void setDimension(Dimension dimension) {
    this.width = String.valueOf(dimension.getWidth());
    this.height = String.valueOf(dimension.getHeight());
  }

  @Override
  public String getName() {
    return "scale";
  }

  @Override
  public List<Pair<String, String>> getValues() {
    return List.of(Pair.of("width", width), Pair.of("height", height));
  }
}
