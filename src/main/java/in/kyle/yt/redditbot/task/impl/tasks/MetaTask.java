package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import in.kyle.yt.redditbot.music.TimedSong;
import in.kyle.yt.redditbot.reddit.generator.meta.RedditVideoMetaGenerator;
import in.kyle.yt.redditbot.reddit.generator.tags.RedditKeywordGenerator;
import in.kyle.yt.redditbot.reddit.generator.tags.RedditTagGenerator;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(4)
class MetaTask implements Task {

  private final RedditVideoMetaGenerator metaGenerator;
  private final RedditTagGenerator tagGenerator;
  private final RedditKeywordGenerator keywordGenerator;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    RedditThread thread = context.getThread();
    List<TimedSong> songs = context.getSongs();

    var tags = tagGenerator.getTags(thread);
    String description = metaGenerator.generateDescription(thread, songs, tags);
    String title = metaGenerator.generateTitle(thread);
    String comment = metaGenerator.generateComment(thread);
  
    List<String> keywords = keywordGenerator.getKeywords(thread);

    return context
        .withTags(tags)
        .withDescription(description)
        .withTitle(title).withComment(comment).withKeywords(keywords);
  }
}
