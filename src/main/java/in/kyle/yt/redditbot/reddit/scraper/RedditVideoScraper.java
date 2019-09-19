package in.kyle.yt.redditbot.reddit.scraper;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.reddit.db.RedditVideoHistory;
import in.kyle.yt.redditbot.reddit.generator.video.RedditVideoSettings;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.model.Subreddit;
import in.kyle.yt.redditbot.reddit.scraper.filter.RedditCommentFilters;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.tts.TtsDuration;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class RedditVideoScraper {

  private final RedditScraper scraper;
  private final RedditCommentFilters filters;
  private final RedditVideoSettings settings;
  private final TtsDuration ttsDuration;
  private final RedditVideoHistory history;

  public ThreadCommentPair getFilteredPair(Subreddit subreddit) {
    List<RedditThread> threads = getFilteredThreads(subreddit);
    Conditions.isTrue(threads.size() != 0, "No threads found");
    for (RedditThread thread : threads) {
      List<RedditComment> filteredComments = getCommentsForVideo(thread);
      if (filteredComments != null) {
        return new ThreadCommentPair(thread, filteredComments);
      }
    }
    throw Conditions.error("No threads found with enough valid comments");
  }

  public RedditThread getFilteredThread(Subreddit subreddit) {
    List<RedditThread> filteredThreads = getFilteredThreads(subreddit);
    Conditions.isTrue(filteredThreads.size() != 0, "No threads found");
    return filteredThreads.get(0);
  }

  private List<RedditComment> getCommentsForVideo(RedditThread thread) {
    Duration duration = new Duration(0);
    List<RedditComment> result = new ArrayList<>();
    Iterator<RedditComment> filteredComments = getFilteredComments(thread).iterator();
    while (duration.lt(settings.getTargetTime()) && filteredComments.hasNext()) {
      RedditComment comment = filteredComments.next();
      result.add(comment);
      duration = duration.add(ttsDuration.getDuration(comment.getContentAsText()));
    }
    if (duration.lt(settings.getMinTime())) {
      return null;
    } else {
      return result;
    }
  }

  public Subreddit getSubreddit(String name) {
    return scraper.getSubreddit(name);
  }

  private List<RedditThread> getFilteredThreads(Subreddit subreddit) {
    List<RedditThread> threads = scraper.getAllPossibleThreads(subreddit);
    return filterThreads(threads);
  }

  private List<RedditComment> getFilteredComments(RedditThread thread) {
    return filterComments(scraper.getComments(thread, thread.getSubreddit()));
  }

  private List<RedditThread> filterThreads(List<RedditThread> input) {
    return input.stream()
        .filter(filters::allow)
        .filter(history::isUnique)
        .sorted((t1, t2) -> Integer.compare(t2.getScore(), t1.getScore()))
        .collect(Collectors.toList());
  }

  private List<RedditComment> filterComments(List<RedditComment> comments) {
    return comments.stream().filter(filters::allow).collect(Collectors.toList());
  }

  @Value
  public static class ThreadCommentPair {
    RedditThread thread;
    List<RedditComment> comments;
  }
}
