package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.springframework.stereotype.Component;

@Component
class FfmpegCommandPrinter {

  String prettyPrintCommand(String command) {
    return command.replaceAll("(?<input>-i .*?\\.[^ ]+)", "${input} `\n").replace("\"", "`\n\"");
  }
}
