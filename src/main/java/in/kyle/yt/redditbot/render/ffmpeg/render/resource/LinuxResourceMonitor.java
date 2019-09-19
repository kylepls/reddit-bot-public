package in.kyle.yt.redditbot.render.ffmpeg.render.resource;

import com.opencsv.CSVReader;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
@Conditional(LinuxCondition.class)
public class LinuxResourceMonitor implements ResourceMonitor {

  private static final int MEM_INDEX = 9;
  private static final int CPU_INDEX = 8;

  @SneakyThrows
  public ResourceUsage getUsage(long pid) {
    var process = new ProcessBuilder(makeCommand(pid)).start();
    CSVReader reader = new CSVReader(new InputStreamReader(process.getInputStream()));
    List<String[]> lines = reader.readAll();
    if (lines.size() != 0) {
      String[] row = lines.get(0);
      return new ResourceUsage(row[CPU_INDEX], row[MEM_INDEX]);
    } else {
      return new ResourceUsage("-1", "-1");
    }
  }

  private List<String> makeCommand(long pid) {
    return List.of(
        "top",
        "-p",
        String.valueOf(pid),
        "-b",
        "-n",
        "1",
        "|",
        "sed",
        "-n",
        "'8, 12{s/^ *//;s/ *$//;s/  */,/gp;};12q'");
  }
}
