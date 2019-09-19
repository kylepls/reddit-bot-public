package in.kyle.yt.redditbot.keyword;

import java.util.List;

public interface KeywordExtractor {

  List<String> getKeywords(String text);
}
