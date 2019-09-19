package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import in.kyle.yt.redditbot.render.ffmpeg.ConcatRenderer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
class FfmpegConcatRenderer implements ConcatRenderer {

  private final FfmpegSettings settings;
  private final FfmpegRenderExecutor executor;
  
  @Override
  public void concat(List<Path> files, Path renderDir, Path outputFile, long totalDuration) {
    Path concat = makeConcatFile(renderDir, files);
    var command =
        new ArrayList<>(
            List.of(settings.getExecutable(), "-f", "concat", "-i", toUnixPath(concat)));
    command.addAll(List.of("-y", "-stats", "-hide_banner", "-loglevel", "error"));
    command.add(toUnixPath(outputFile));
    executor.execute(command, totalDuration);
  }

  private String toUnixPath(Path path) {
    return path.toString().replace("\\", "/");
  }

  @SneakyThrows
  private Path makeConcatFile(Path renderFolder, List<Path> concatFiles) {
    Path concatFile = renderFolder.resolve(UUID.randomUUID().toString() + ".ffconcat");
    String string = concatFiles.stream()
            .map(p -> p.getFileName().toString())
            .filter(s -> s.endsWith(".mp4"))
            .map(s -> "file " + s)
            .collect(Collectors.joining("\n"));
    Files.write(concatFile, string.getBytes());
    return concatFile;
  }
}
