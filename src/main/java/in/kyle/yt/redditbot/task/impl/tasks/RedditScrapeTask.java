package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.reddit.generator.video.RedditVideoSettings;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.model.Subreddit;
import in.kyle.yt.redditbot.reddit.scraper.RedditVideoScraper;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.tts.TtsDuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(0)
class RedditScrapeTask implements Task {

  private final RedditVideoScraper redditVideoScraper;
  private final RedditVideoSettings videoSettings;
  private final TtsDuration duration;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    Subreddit subreddit = redditVideoScraper.getSubreddit("AskReddit");
    RedditVideoScraper.ThreadCommentPair pair = redditVideoScraper.getFilteredPair(subreddit);
    RedditThread thread = pair.getThread();
    List<RedditComment> comments = pair.getComments();

    Duration totalDuration =
        comments.stream()
            .map(c -> duration.getDuration(c.getContentAsText()))
            .reduce(new Duration(0), Duration::add);
    log.info("Got {} comments with total runtime of ~{}", comments.size(), totalDuration);

    Conditions.isTrue(
        comments.size() >= videoSettings.getMinComments(),
        "Not enough comments for thread {} got {}",
        thread.getIdentifier(),
        comments.size());

    comments = comments.stream().limit(videoSettings.getMaxComments()).collect(Collectors.toList());

    log.info("Thread {}:'{}'", thread.getIdentifier(), thread.getTitle());
    log.info("- Comments {}", comments.size());

    return context.toBuilder().thread(thread).comments(comments).build();
  }
}
