package in.kyle.yt.redditbot.timeline;

import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Overlay {

  private final Map<String, String> args = new HashMap<>();

  public List<Pair<String, String>> makeArgs() {
    return args.entrySet().stream()
        .map(e -> Pair.of(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  public void setPosition(String x, String y, boolean isStatic) {
    args.put("x", x);
    args.put("y", y);
    if (isStatic) {
      args.put("eval", "init");
    } else {
      args.put("eval", "frame");
    }
  }
}
