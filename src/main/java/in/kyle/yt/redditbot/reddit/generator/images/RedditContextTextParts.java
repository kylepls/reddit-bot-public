package in.kyle.yt.redditbot.reddit.generator.images;

import java.util.List;
import java.util.Map;

import in.kyle.yt.redditbot.reddit.model.RedditComment;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class RedditContextTextParts {

  List<String> titleParts;

  @Singular Map<RedditComment, List<String>> commentParts;
}
