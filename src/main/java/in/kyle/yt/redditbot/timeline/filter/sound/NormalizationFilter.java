package in.kyle.yt.redditbot.timeline.filter.sound;

import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.filter.model.AudioFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NormalizationFilter implements AudioFilter {

  private float target = -24f;

  @Override
  public String getName() {
    return "loudnorm";
  }

  @Override
  public List<Pair<String, String>> getValues() {
    return List.of(Pair.of("i", String.format("%.4f", target)));
  }
}
