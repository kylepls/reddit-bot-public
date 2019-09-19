package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Path;
import java.util.List;

import javax.validation.constraints.Min;

import lombok.Data;

@ConfigurationProperties(prefix = "ffmpeg")
@Data
@Validated
public class FfmpegSettings {

  private final String executable;
  private final String vcodec;
  private final String acodec;
  private final String tempFolder;

  @Min(0)
  private final float frameRate;

  private final String flags;

  public List<String> getFlagsList() {
    return List.of(flags.split(" "));
  }

  public Path getTempFolder() {
    return Path.of(tempFolder);
  }
}
