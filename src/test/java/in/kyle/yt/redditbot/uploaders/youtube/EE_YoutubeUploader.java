package in.kyle.yt.redditbot.uploaders.youtube;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import in.kyle.yt.redditbot.uploaders.Video;
import in.kyle.yt.redditbot.utils.Resources;
import in.kyle.yt.redditbot.utils.spring.TestApplicationConfiguration;
import lombok.val;

@SpringBootTest(classes = TestApplicationConfiguration.class)
class EE_YoutubeUploader {

  @Autowired YoutubeUploader youtubeUploader;
  
  private File file;

  @BeforeEach
  void setup() throws IOException {
    ClassPathResource resource = Resources.getResource("upload.mp4", getClass());
    file = resource.getFile();
  }

  @Test
  void testUpload() throws IOException {
    val meta =
        Video.builder()
            .description("Hello World!")
            .tag("tag1")
            .tag("t2")
            .tag("animals").keyword("hello").keyword("world")
            .title("A test youtube upload")
            .videoLength(file.length())
            .videoStream(new FileInputStream(file))
            .build();

    youtubeUploader.upload(meta);
  }

  @Test
  void testLikeVideo() throws IOException {
    youtubeUploader.likeVideo("BcmRf4i1ugg");
  }

  @Test
  void testTestMakeComment() throws IOException {
    youtubeUploader.addFirstComment("BcmRf4i1ugg", "Test comment");
  }
}
