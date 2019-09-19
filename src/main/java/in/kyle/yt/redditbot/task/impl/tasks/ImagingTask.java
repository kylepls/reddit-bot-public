package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import in.kyle.yt.redditbot.reddit.generator.images.RedditImager;
import in.kyle.yt.redditbot.reddit.generator.thumbnail.RedditThumbnailGenerator;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
class ImagingTask implements Task {

  private final RedditImager imager;
  private final RedditThumbnailGenerator thumbnailGenerator;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    RedditThread thread = context.getThread();
    List<RedditComment> comments = context.getComments();
    RenderDirectory directory = RenderDirectory.newDirectory(thread.getIdentifier());

    log.info("Generating images to {}", directory.getImages());
    var textParts = imager.generateImages(directory.getImages(), thread, comments);
    thumbnailGenerator.generate(thread, directory.getThumbnail());

    return context.withDirectory(directory).withTextParts(textParts);
  }
}
