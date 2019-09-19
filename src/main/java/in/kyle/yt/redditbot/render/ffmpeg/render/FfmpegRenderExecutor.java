package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.stereotype.Component;

import java.util.List;

import in.kyle.yt.redditbot.render.ffmpeg.render.resource.ResourceMonitor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class FfmpegRenderExecutor {

  private final FfmpegCommandPrinter printer;
  private final ResourceMonitor resourceMonitor;

  @SneakyThrows
  void execute(List<String> commands, long totalTime) {
    var builder = new ProcessBuilder();
    Process process = builder.command(commands).start();
    String command = process.info().commandLine().orElseGet(() -> String.join(" ", commands));
    log.info("Command: \n{}", printer.prettyPrintCommand(command));
    Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));
    FfmpegStatus status =
        new FfmpegStatus(process.getErrorStream(), System.out, process.pid(), resourceMonitor);
    status.showStatusString(totalTime);
    status.run();
    process.waitFor();
  }
}
