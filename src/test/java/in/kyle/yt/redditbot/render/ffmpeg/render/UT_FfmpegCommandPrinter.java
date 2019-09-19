package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UT_FfmpegCommandPrinter {

  private final FfmpegCommandPrinter sut = new FfmpegCommandPrinter();

  @Test
  void printCommand() {
    String inputSting = "a b c -i path/to/file1.png";
    String output = sut.prettyPrintCommand(inputSting + " " + inputSting);
    String outputString = "a b c -i path/to/file1.png `\n";
    assertThat(output.trim()).isEqualTo(StringUtils.repeat(outputString, " ", 2).trim());
  }
}
