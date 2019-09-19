package in.kyle.yt.redditbot.tts;

import java.nio.file.Path;

public interface TtsEngine {

  default void generateSpeech(String input, Path output) {
    generateSpeech(input, output, 1F);
  }

  void generateSpeech(String input, Path output, float speed);
}
