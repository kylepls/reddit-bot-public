package in.kyle.yt.redditbot.render;

import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class RenderDirectory {

  private final Path directory;
  private final Path images;
  private final Path sounds;
  private final Path cards;
  private final Path thumbnail;
  private final Path outputFile;
  private final Path meta;
  private final Path render;

  RenderDirectory(Path directory) {
    this.directory = directory;
    mkdirs(directory);
    this.images = file("images");
    this.sounds = file("sounds");
    this.cards = file("cards");
    this.thumbnail = file("thumbnail.png");
    this.outputFile = file("out.mp4");
    this.meta = file("meta.json");
    this.render = file("render.json");
  }

  public static RenderDirectory newDirectory(String name) {
    return newDirectory(Path.of("temp", name));
  }

  public static RenderDirectory newDirectory(Path path) {
    RenderDirectory directory = new RenderDirectory(path);
    directory.mkdirs();
    return directory;
  }

  private Path file(String name) {
    return directory.resolve(name);
  }

  private void mkdirs() {
    mkdirs(directory);
    mkdirs(images);
    mkdirs(sounds);
    mkdirs(cards);
    mkdirs(render);
  }

  @SneakyThrows
  private void mkdirs(Path path) {
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
  }
}
