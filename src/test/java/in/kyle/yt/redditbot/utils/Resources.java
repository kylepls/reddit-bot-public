package in.kyle.yt.redditbot.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@UtilityClass
public class Resources {

  private static final Path TEST_DIRECTORY = Path.of("target", "tests");

  @SneakyThrows
  public static Path copyResources(Class<?> test) {
    Path testDirectory = getTestDirectory(test);
    ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
    String resourceDirectory = test.getName().replace(".", "/");
    String locationPattern = String.format("classpath*:%s/**", resourceDirectory);
    Resource[] resources = patternResolver.getResources(locationPattern);
    for (Resource resource : resources) {
      File file = resource.getFile();
      if (!file.isDirectory()) {
        String resourcePath = file.toPath().toString();
        Path relativePath =
            Path.of(
                resourcePath.substring(
                    resourcePath.lastIndexOf(test.getSimpleName())
                        + test.getSimpleName().length()
                        + 1));
        Path targetPath = testDirectory.resolve(relativePath).toAbsolutePath();
        if (!Files.exists(targetPath.getParent())) {
          Files.createDirectories(targetPath);
        }
        Files.copy(resource.getInputStream(), targetPath, REPLACE_EXISTING);
      }
    }

    return testDirectory;
  }

  @SneakyThrows
  public static void copyResource(String name, Class<?> test, Path target) {
    var resource = getResource(name, test);
    Files.copy(resource.getInputStream(), target, REPLACE_EXISTING);
  }

  @SneakyThrows
  public static ClassPathResource getResource(String name, Class<?> test) {
    return new ClassPathResource(name, test);
  }

  public static Path getTestDirectory(Class<?> test) throws IOException {
    Path path = TEST_DIRECTORY.resolve(test.getName());
    Files.createDirectories(path);
    return path;
  }

  private static Path getCleanTestDirectory(Class<?> test) throws IOException {
    Path path = TEST_DIRECTORY.resolve(test.getName());
    cleanDirectory(path);
    Files.createDirectories(path);
    return path;
  }

  @SneakyThrows
  public static void cleanupTestDirectories() {
    cleanDirectory(TEST_DIRECTORY);
  }

  private static void cleanDirectory(Path path) throws IOException {
    if (Files.exists(path)) {
      FileSystemUtils.deleteRecursively(path);
    }
  }
}
