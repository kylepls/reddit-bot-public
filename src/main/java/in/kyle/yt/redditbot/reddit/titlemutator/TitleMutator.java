package in.kyle.yt.redditbot.reddit.titlemutator;

import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TitleMutator {

  private final List<Mutator> mutators;

  public String getEffectiveTitle(String title) {
    String result = title;
    for (Mutator mutator : mutators) {
      result = mutator.mutate(result);
    }
    return result;
  }
}
