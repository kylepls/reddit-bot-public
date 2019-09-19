package in.kyle.yt.redditbot.task.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class VideoGenerator implements Runnable {

  private final List<Task> steps;
  private final Path contextFile;
  private final Gson gson;

  private VideoGenContext context = VideoGenContext.builder().build();
  private int step = 0;

  public VideoGenerator(List<Task> steps, Path contextFile, Gson gson) {
    this.steps = steps;
    this.contextFile = contextFile;
    this.gson = gson;
  }

  @Override
  public void run() {
    for (; step < steps.size(); ) {
      Task runnable = steps.get(step);
      log.info("Executing step {}:{}", step, runnable.getClass().getSimpleName());
      try {
        VideoGenContext newContext = runnable.process(context);
        context = newContext;
        step++;
        saveContext();
      } catch (Exception e) {
        throw new RuntimeException(
            "Could not execute step " + step + ": " + runnable.getClass().getSimpleName(), e);
      }
    }
  }

  private void saveContext() throws IOException {
    JsonObject object = gson.toJsonTree(context).getAsJsonObject();
    object.addProperty("step", step);
    Files.write(contextFile, gson.toJson(object).getBytes());
  }
}
