package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import in.kyle.yt.redditbot.music.TimedSong;
import in.kyle.yt.redditbot.reddit.db.PersistableSong;
import in.kyle.yt.redditbot.reddit.db.PersistedVideo;
import in.kyle.yt.redditbot.reddit.db.RedditVideoHistory;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import lombok.RequiredArgsConstructor;

@Component
@Order(6)
@RequiredArgsConstructor
public class PersistTask implements Task {

  private final RedditVideoHistory history;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    PersistedVideo video = makePersistable(context);
    history.save(video);
    return context;
  }

  private PersistedVideo makePersistable(VideoGenContext context) {
    var songs =
        context.getSongs().stream().map(this::makePersistableSong).collect(Collectors.toList());
    return new PersistedVideo(
        context.getThread(),
        context.getComments(),
        songs,
        context.getTags(),
        context.getTitle(),
        context.getDescription());
  }

  private PersistableSong makePersistableSong(TimedSong s) {
    return new PersistableSong(
        s.getSong().getAuthor(), s.getSong().getName(), s.getSong().getDuration(), s.getStart());
  }
}
