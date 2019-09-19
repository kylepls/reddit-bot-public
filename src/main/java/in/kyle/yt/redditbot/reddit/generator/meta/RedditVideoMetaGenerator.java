package in.kyle.yt.redditbot.reddit.generator.meta;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Service;
import org.stringtemplate.v4.AttributeRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;
import java.util.Locale;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.music.TimedSong;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.timeline.Timestamp;
import lombok.SneakyThrows;

@Service
public class RedditVideoMetaGenerator {

  private final STGroup redditTemplate;

  @SneakyThrows
  public RedditVideoMetaGenerator(RedditVideoMetaSettings settings) {
    this.redditTemplate = new STGroupFile(settings.getTemplate().getURL());
    this.redditTemplate.registerRenderer(Timestamp.class, new TimestampRenderer());
  }

  public String generateTitle(RedditThread thread) {
    ST title = redditTemplate.getInstanceOf("formatTitle");
    title.add("thread", thread);
    String render = title.render();
    //        Conditions.isTrue(render.length() <= 70, "Title length must be <70, {}",
    // render.length());
    return render;
  }

  public String generateDescription(RedditThread thread, List<TimedSong> songs, List<String> tags) {
    ST description = redditTemplate.getInstanceOf("description");
    description.add("thread", thread);
    description.add("songs", songs);
    description.add("tags", tags);
    String render = description.render();
    Conditions.isTrue(render.length() <= 5000, "Description must <=5000 chars");
    return render;
  }

  public String generateComment(RedditThread thread) {
    ST comment = redditTemplate.getInstanceOf("comment");
    comment.add("title", thread.getTitle());
    return comment.render();
  }

  static class TimestampRenderer implements AttributeRenderer {
    @Override
    public String toString(Object o, String formatString, Locale locale) {
      Timestamp ts = (Timestamp) o;
      return DurationFormatUtils.formatDuration(ts.getMillis(), "mm:ss");
    }
  }
}
