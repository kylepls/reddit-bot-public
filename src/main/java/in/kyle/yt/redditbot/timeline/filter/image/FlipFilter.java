package in.kyle.yt.redditbot.timeline.filter.image;

import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.filter.model.ImageFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class FlipFilter implements ImageFilter {
  private final Type type;

  @Override
  public String getName() {
    return type.getLetter() + "flip";
  }

  @Override
  public List<Pair<String, String>> getValues() {
    return List.of();
  }

  @Getter
  @AllArgsConstructor
  public enum Type {
    VERTICAL("v"),
    HORIZONTAL("h");

    private final String letter;
  }
}
