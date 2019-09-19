package in.kyle.yt.redditbot.tts.polly;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UT_SsmlFormatter {

  private final SsmlFormatter formatter = new SsmlFormatter();

  @Test
  void testEscapeSequence() {
    String string = "&test</prosody>";
    String ssml = formatter.formatSsml(string, 0.699F);

    assertThat(ssml).isEqualTo("<prosody rate=\"70%\">&amp;test&lt;/prosody&gt;</prosody>");
  }
}
