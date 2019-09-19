package in.kyle.yt.redditbot.reddit.generator;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.reddit.generator.images.RedditContextTextParts;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.tts.TtsEngine;
import lombok.RequiredArgsConstructor;

@Lazy
@Service
@RequiredArgsConstructor
public class RedditTtsGenerator {

  private final TtsEngine engine;

  public void generateTts(Path soundFolder, RedditContextTextParts textParts) {
    incrementalSound(soundFolder, "title", textParts.getTitleParts());

    for (var entry : textParts.getCommentParts().entrySet()) {
      RedditComment comment = entry.getKey();
      List<String> parts = entry.getValue();
      incrementalSound(soundFolder, comment.getIdentifier(), parts);
    }
  }

  private void incrementalSound(Path soundFolder, String fileName, List<String> parts) {
    int i = 1;
    for (String part : parts) {
      Path commentSoundFile = soundFolder.resolve(String.format("%s%d.mp3", fileName, i++));
      engine.generateSpeech(part, commentSoundFile);
    }
  }
}
