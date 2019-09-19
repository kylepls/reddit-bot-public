package in.kyle.yt.redditbot.reddit.titlemutator;

import org.springframework.stereotype.Component;

@Component
class PronunciationRemover implements Mutator {
  @Override
  public String mutate(String title) {
    return title.replaceAll("[`!’'?.]", "");
  }
}
