package in.kyle.yt.redditbot.timeline.filter.sound;

import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.filter.model.AudioFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VolumeFilter implements AudioFilter {

  // accepts percents 0.0-1 and dB
  private String volume = "1";

  @Override
  public String getName() {
    return "volume";
  }

  @Override
  public List<Pair<String, String>> getValues() {
    return List.of(Pair.of("volume", volume));
  }
}
