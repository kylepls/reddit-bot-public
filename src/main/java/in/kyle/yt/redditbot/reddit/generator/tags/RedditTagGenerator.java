package in.kyle.yt.redditbot.reddit.generator.tags;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class RedditTagGenerator {
    
    private final RedditTagSettings settings;
    private final Gson gson;
    
    @Retryable(backoff = @Backoff(multiplier = 2, value = 3000))
    @SneakyThrows
    public List<String> getTags(RedditThread thread) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(makeUri(thread.getTitle())).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<String> generatedTags = gson.fromJson(response.body(), new TypeToken<List<String>>() {
        }.getType());
        generatedTags.removeIf(s -> s.length() > 40);
        generatedTags.removeAll(settings.getStartingTags());
        generatedTags.removeAll(settings.getEndingTags());
    
        List<String> tags = new ArrayList<>();
        tags.addAll(settings.getStartingTags());
        //        tags.addAll(generatedTags);
        tags.addAll(settings.getEndingTags());
    
        return tags;
    }
    
    @SneakyThrows
    private URI makeUri(String input) {
        String url = "https://rapidtags.io/api/index.php";
        return new URIBuilder(url).addParameter("tool", "tag-generator")
                .addParameter("input", input)
                .build();
  }
}
