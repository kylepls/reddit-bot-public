package in.kyle.yt.redditbot.tts.polly;

import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.tts.TtsDuration;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class AmazonPollyTtsDuration implements TtsDuration {

  private final AmazonPollySettings settings;

  @Override
  public Duration getDuration(String text) {
    float wpms = (settings.getWpm() / 60F) / 1000F;
    int words = text.split("\\s+").length;
    return new Duration((long) (words / wpms));
  }
}
