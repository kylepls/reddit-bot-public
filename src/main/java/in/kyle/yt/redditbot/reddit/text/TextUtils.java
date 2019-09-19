package in.kyle.yt.redditbot.reddit.text;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextUtils {

  public static String removeHtml(String html) {
    return html.replaceAll("<[^>]*>", "");
  }

  public static String unescapeEntities(String text) {
    return Parser.unescapeEntities(text, true);
  }

  public static Element parse(String htmlInput) {
    Document parse = Jsoup.parse(htmlInput, "", Parser.xmlParser());
    if (parse.isBlock()) {
      return parse.child(0);
    } else {
      return parse;
    }
  }
}
