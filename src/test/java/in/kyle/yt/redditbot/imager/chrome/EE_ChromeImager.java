package in.kyle.yt.redditbot.imager.chrome;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.imager.ImagerCommand;
import in.kyle.yt.redditbot.imager.ImagerResult;
import in.kyle.yt.redditbot.utils.Resources;

@SpringBootTest
class EE_ChromeImager {

  @Autowired ChromeImager imager;

  Path outputDir;

  @BeforeEach
  void setup() throws IOException {
    outputDir = Resources.getTestDirectory(getClass());
  }

  @Test
  void test() {
    ImagerCommand command =
        ImagerCommand.builder()
            .incremental(true)
            .outputFilePrefix(outputDir.resolve("out"))
            .parentCssSelector("#t3_cx0cy3 > div")
            .textCssSelector("#t3_cx0cy3 h1")
            .payload("")
            .url(
                "https://www.reddit.com/r/AskReddit/comments/cx0cy3/logically_morally_humanely_what_should_be_free/")
            .build();
    List<ImagerResult> execute = imager.execute(List.of(command));
    System.out.println(execute);
  }
}
