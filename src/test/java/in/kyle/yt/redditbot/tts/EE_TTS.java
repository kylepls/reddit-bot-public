package in.kyle.yt.redditbot.tts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;

import in.kyle.yt.redditbot.utils.Resources;

@SpringBootTest
class EE_TTS {

  @Autowired TtsEngine ttsEngine;
  
  private Path outputFile;

  @BeforeEach
  void setup() throws IOException {
    outputFile = Resources.getTestDirectory(getClass()).resolve("out.mp3");
  }

  @Test
  void test() {
    ttsEngine.generateSpeech("hello world", outputFile);
    System.out.println(outputFile);
  }
}
