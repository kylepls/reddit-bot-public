package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import in.kyle.yt.redditbot.render.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class FfmpegRenderer {

  private final FfmpegSettings settings;
    private final FileValidator validator;
  private final FfmpegRenderExecutor renderExecutor;

  void run(FfmpegCommand command) {
    List<String> arguments = new ArrayList<>();
    arguments.add(settings.getExecutable());
    arguments.addAll(command.getInputArgs());
    arguments.addAll(
        List.of("-filter_complex_script", createFilterScriptFile(command.getFilterGraph())));
    arguments.addAll(command.getPostFilterArgs());
    arguments.add(command.getOutputFile().toString());

    log.info("Running command...");
    renderExecutor.execute(arguments, command.getTotalVideoTime());
    validator.validate(command.getOutputFile());
  }

  @SneakyThrows
  private String createFilterScriptFile(String filterGraph) {
    Path filterComplexScript =
        Path.of("temp", "ffmpeg", "filter-" + UUID.randomUUID().toString() + ".txt");
    Files.createDirectories(filterComplexScript.getParent());
    Files.write(filterComplexScript, filterGraph.getBytes());
    return filterComplexScript.toString();
  }
}
