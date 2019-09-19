package in.kyle.yt.redditbot.tts.polly;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

@Component
class SsmlFormatter {

  String formatSsml(String text, float speed) {
    var escaped = StringEscapeUtils.escapeXml(text);
    return String.format("<prosody rate=\"%.0f%%\">%s</prosody>", speed * 100, escaped);
  }
}
