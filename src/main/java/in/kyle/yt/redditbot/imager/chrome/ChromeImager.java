package in.kyle.yt.redditbot.imager.chrome;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.imager.Imager;
import in.kyle.yt.redditbot.imager.ImagerCommand;
import in.kyle.yt.redditbot.imager.ImagerResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class ChromeImager implements Imager {

  private final Gson gson;
  private final ChromeSettings settings;

  @Override
  @SneakyThrows
  @Retryable(backoff = @Backoff(delay = 2000, multiplier = 2))
  public List<ImagerResult> execute(List<ImagerCommand> commands) {
    List<ImagerResult> results = new ArrayList<>();
    String response = sendRequest(generateConfiguration(commands));

    JsonArray elements = gson.fromJson(response, JsonArray.class);
    int index = 0;
    for (JsonElement element : elements) {
      ImagerCommand command = commands.get(index++);
      JsonObject object = element.getAsJsonObject();
      JsonArray images = object.get("images").getAsJsonArray();
      results.add(loadImages(command, images));
    }

    return results;
  }

  private String sendRequest(String configuration) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(settings.getEndpoint())
            .POST(HttpRequest.BodyPublishers.ofString(configuration))
            .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    Conditions.isTrue(
        response.statusCode() == 200,
        "Non 200 response code: {} - {}",
        response.statusCode(),
        response.body());
    Conditions.isTrue(
        response.body().startsWith("["),
        "Invalid imager response: {}\nConfiguration: {}",
        response,
        configuration);
    return response.body();
  }

  private ImagerResult loadImages(ImagerCommand command, JsonArray images) throws IOException {
    List<String> parts = new ArrayList<>();
    List<Path> outputFiles = new ArrayList<>();
    int index = 1;

    for (JsonElement imageElement : images) {
      JsonObject image = imageElement.getAsJsonObject();
      byte[] bytes = Base64.getDecoder().decode(image.get("b64").getAsString());
      Path outputFile = pathAppend(command.getOutputFilePrefix(), index++ + ".png");
      Files.write(outputFile, bytes, StandardOpenOption.CREATE);

      String part = image.get("part").getAsString();
      parts.add(part);
      outputFiles.add(outputFile);
    }
    return new ImagerResult(parts, outputFiles);
  }

  private Path pathAppend(Path path, String append) {
    return Path.of(path.toString() + append);
  }

  @SneakyThrows
  private String generateConfiguration(List<ImagerCommand> commands) {
    return gson.toJson(commands);
  }
}
