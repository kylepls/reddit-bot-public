package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import in.kyle.yt.redditbot.music.BackgroundMusicGenerator;
import in.kyle.yt.redditbot.reddit.generator.video.RedditTimelineGenerator;
import in.kyle.yt.redditbot.render.IncrementalTimelineRenderer;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import in.kyle.yt.redditbot.timeline.Timeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(3)
class RenderTask implements Task {

  private final RedditTimelineGenerator video;
  private final IncrementalTimelineRenderer renderer;
  private final BackgroundMusicGenerator backgroundMusic;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    RenderDirectory directory = context.getDirectory();
    log.info("Generating timeline...");
    Timeline timeline = video.generateTimeline(directory, context.getComments());
    var songs = backgroundMusic.addBackgroundMusic(timeline, directory);
    log.info("Rendering timeline...");
    renderer.render(timeline, directory.getOutputFile(), context.getDirectory().getRender());
    return context.withSongs(songs);
  }
}
