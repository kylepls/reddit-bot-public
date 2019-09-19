package in.kyle.yt.redditbot.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
class GsonFactory {

  private final List<GsonAdapter> adapters;

  @Bean
  public Gson gson() {
    var builder = new GsonBuilder();
    adapters.forEach(a -> builder.registerTypeAdapter(a.getType(), a));
    builder.disableHtmlEscaping();
    builder.setPrettyPrinting();
    return builder.create();
  }
}
