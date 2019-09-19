package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.reddit.generator.RedditTtsGenerator;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2)
class TtsTask implements Task {

  private final RedditTtsGenerator tts;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    RenderDirectory directory = context.getDirectory();

    log.info("Generating sounds to {}", directory.getSounds());
    tts.generateTts(directory.getSounds(), context.getTextParts());

    return context;
  }
}
