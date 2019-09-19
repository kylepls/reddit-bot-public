package in.kyle.yt.redditbot.task.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TaskFolder {

  @SneakyThrows
  public static Path newTaskFile() {
    Path folder = Path.of("temp", "tasks");
    Files.createDirectories(folder);
    return folder.resolve(String.format("run-%tc.json", new Date()).replace(":", "_"));
  }
}
