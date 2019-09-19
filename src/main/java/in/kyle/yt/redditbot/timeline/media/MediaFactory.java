package in.kyle.yt.redditbot.timeline.media;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.render.FileValidator;

@Component
public class MediaFactory {

  private final Map<String, MediaProvider<?>> providers = new HashMap<>();
  private final FileValidator validator;
  
  public MediaFactory(List<MediaProvider> providers, FileValidator validator) {
    this.validator = validator;
    providers.forEach(this::register);
  }

  private void register(MediaProvider provider) {
    for (String extension : provider.getExtensions()) {
      providers.put(extension, provider);
    }
  }

  public <T extends Media> T load(Path file) {
    Conditions.isTrue(Files.exists(file), "File {} not found", file.toAbsolutePath());
    validator.validate(file);
    String fileName = file.getFileName().toString();
    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
    MediaProvider<T> provider = (MediaProvider<T>) providers.get(extension);
    Conditions.notNull(
        provider, "No provider for {}\nProviders: {}", extension, providers.keySet());
    return provider.load(file);
  }
}
