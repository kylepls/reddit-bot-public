package in.kyle.yt.redditbot.render.ffmpeg.render.resource;

import com.opencsv.CSVReader;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.List;

import lombok.SneakyThrows;

@Component
@Conditional(WindowsCondition.class)
public class WindowsResourceMonitor implements ResourceMonitor {

  @Override
  @SneakyThrows
  public ResourceUsage getUsage(long pid) {
    var process = new ProcessBuilder(makeCommand()).start();
    CSVReader reader = new CSVReader(new InputStreamReader(process.getInputStream()));
    List<String[]> lines = reader.readAll();
    String usage =
        lines.stream()
            .skip(1)
            .filter(p -> Long.parseLong(p[1]) == pid)
            .findFirst()
            .map(p -> p[4])
            .orElse("-1");
    return new ResourceUsage("-1", usage);
  }

  private List<String> makeCommand() {
    return List.of("tasklist", "/FO", "csv");
  }
}
