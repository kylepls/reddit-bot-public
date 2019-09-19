package in.kyle.yt.redditbot.reddit.generator.images;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.imager.Imager;
import in.kyle.yt.redditbot.imager.ImagerResult;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.utils.Resources;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EE_RedditImager {

  @Autowired RedditImager redditImager;

  @Autowired Imager imager;

  private Path outputDir;

  @BeforeEach
  void setup() throws IOException {
    outputDir = Resources.getTestDirectory(getClass());
  }

  @Test
  void test() throws IOException {
    String link =
        "https://www.reddit.com/r/AskReddit/comments/cwj2wv/what_is_something_that_makes_you_say_man_i_fing/eybwckf/";
    var comment = RedditComment.builder().identifier("eybwckf").link(link).build();

    var command = redditImager.makeCommentCommand(outputDir, comment);
    List<ImagerResult> result = imager.execute(List.of(command));
    assertThat(result).hasSize(1);
    ImagerResult commandResult = result.get(0);

    int filesCount = (int) Files.list(outputDir).count();
    assertThat(filesCount).isEqualTo(11);
    assertThat(commandResult.getOutputFiles()).hasSize(11);
    assertThat(commandResult.getParts()).hasSize(11);
    System.out.println();
  }
}
