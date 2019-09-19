package in.kyle.yt.redditbot.task;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.TaskFolder;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import in.kyle.yt.redditbot.task.impl.VideoGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Service
@RequiredArgsConstructor
public class RedditGenerateVideoTask {

  private final Gson gson;
  private final List<Task> tasks;

  public void runGeneration() {
    VideoGenerator generator = new VideoGenerator(tasks, TaskFolder.newTaskFile(), gson);
    generator.run();
  }

  @SneakyThrows
  public void continueGeneration(Path contextFile) {
    String json = FileUtils.readFileToString(new File(contextFile.toString()));
    JsonObject object = gson.fromJson(json, JsonObject.class);
    int step = object.get("step").getAsInt();
    VideoGenContext context = gson.fromJson(object, VideoGenContext.class);
    var generator = new VideoGenerator(tasks, contextFile, gson, context, step);
    generator.run();
  }
}
