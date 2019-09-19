package in.kyle.yt.redditbot.tts.polly;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import in.kyle.yt.redditbot.render.FileValidator;
import in.kyle.yt.redditbot.tts.TtsEngine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonPollyTts implements TtsEngine {

  private final AmazonPollySettings pollySettings;
  private final AmazonPolly polly;
  private final SsmlFormatter ssml;
  private final FileValidator fileValidator;
  private Voice voice;

  @PostConstruct
  public void setup() {
    voice = new Voice().withId(pollySettings.getVoiceId());
  }

  @Override
  @SneakyThrows
  @Retryable(value = IOException.class, backoff = @Backoff(delay = 2000, multiplier = 2.5))
  public void generateSpeech(String input, Path output, float speed) {
    InputStream speechStream = synthesize(ssml.formatSsml(input, speed));
    Files.copy(speechStream, output, StandardCopyOption.REPLACE_EXISTING);
    fileValidator.validate(output);
  }

  private InputStream synthesize(String ssml) {
    log.info("Generating mp3 for '{}'", ssml);
    SynthesizeSpeechRequest synthReq =
        new SynthesizeSpeechRequest()
            .withText(ssml)
            .withVoiceId(voice.getId())
            .withTextType("ssml")
            .withOutputFormat(OutputFormat.Mp3);
    SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
    return synthRes.getAudioStream();
  }
}
