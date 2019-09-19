package in.kyle.yt.redditbot.render;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.TestRedditCommment;
import in.kyle.yt.redditbot.utils.Resources;
import lombok.SneakyThrows;

public class TestRenderDirectory {

  @SneakyThrows
  public static RenderDirectory newDirectory(Class<?> test) {
    return RenderDirectory.newDirectory(Resources.getTestDirectory(test));
  }

  @SneakyThrows
  public static RenderDirectory newDirectoryWithResources(Class<?> test) {
    Path directory = Resources.copyResources(test);
    return new RenderDirectory(directory);
  }

  @SneakyThrows
  public static List<RedditComment> getComments(RenderDirectory directory) {
    return Files.list(directory.getImages())
        .map(p -> p.getFileName().toString())
        .filter(f -> !f.contains("title"))
        .map(f -> f.substring(0, 7))
        .map(TestRedditCommment::newComment)
        .collect(Collectors.toList());
  }
}
