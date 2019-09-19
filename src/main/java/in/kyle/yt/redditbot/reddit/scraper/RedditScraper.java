package in.kyle.yt.redditbot.reddit.scraper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.http.client.utils.URIBuilder;
import org.jsoup.parser.Parser;
import org.springframework.data.util.Pair;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import in.kyle.yt.redditbot.reddit.model.RedditAward;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditContent;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.model.Subreddit;
import in.kyle.yt.redditbot.reddit.titlemutator.TitleMutator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
class RedditScraper {

  private final Gson gson;
  private final TitleMutator titleMutator;

  List<RedditThread> getAllPossibleThreads(Subreddit subreddit) {
    return Stream.of(Sort.values())
            .flatMap(s -> Stream.of(Time.values()).map(t -> Pair.of(s, t)))
            .flatMap(pair -> getThreads(subreddit, pair.getFirst(), pair.getSecond()).stream())
            .collect(Collectors.toList());
  }

  @SneakyThrows
  List<RedditThread> getThreads(Subreddit subreddit, Sort sort, Time time) {
    log.info("Scraping {} {} {}", subreddit.getDisplayName(), sort, time);
    String endpoint = String.format("https://www.reddit.com/r/%s/%s.json",
                                    subreddit.getName(),
                                    sort.name().toLowerCase());
    URI uri = new URIBuilder(endpoint).addParameter("limit", "100")
            .addParameter("t", time.name().toLowerCase())
            .build();
    String res = request(uri);
    JsonObject body = gson.fromJson(res, JsonObject.class);
    JsonArray posts = body.getAsJsonObject("data").getAsJsonArray("children");
    Iterator<JsonElement> iterator = posts.iterator();
  
    return makeStream(iterator).map(JsonElement::getAsJsonObject)
            .map(o -> makeThread(o, subreddit))
            .collect(Collectors.toList());
  }

  private <T> Stream<T> makeStream(Iterator<T> iterator) {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
  }

  private RedditThread makeThread(JsonObject json, Subreddit subreddit) {
    JsonObject data = json.get("data").getAsJsonObject();
    String id = data.get("id").getAsString();
    String title = data.get("title").getAsString();
    String author = data.get("author").getAsString();
    int score = data.get("score").getAsInt();
    boolean over18 = data.get("over_18").getAsBoolean();
    String link = String.format("https://www.reddit.com%s", data.get("permalink").getAsString());
    String mutatedTitle = titleMutator.getEffectiveTitle(title);
    JsonElement flairElement = data.get("link_flair_text");
    String flairText = flairElement.isJsonNull() ? "" : flairElement.getAsString();
    return new RedditThread(subreddit,
                            score,
                            title,
                            author,
                            over18,
                            link,
                            getAwards(data),
                            id,
                            mutatedTitle,
                            flairText);
  }

  private Set<RedditAward> getAwards(JsonObject object) {
    Set<RedditAward> result = new HashSet<>();
    JsonArray awardings = object.get("all_awardings").getAsJsonArray();
    for (JsonElement awarding : awardings) {
      JsonObject awardData = (JsonObject) awarding;
      int count = awardData.get("count").getAsInt();
      String iconUrl = awardData.get("icon_url").getAsString();
      result.add(new RedditAward(count, iconUrl));
    }
    return result;
  }

  @SneakyThrows
  List<RedditComment> getComments(RedditContent submission, Subreddit subreddit) {
    String endpoint = "https://www.reddit.com/comments/%s.json";
    String url = String.format(endpoint, submission.getIdentifier());
    URI uri = new URIBuilder(url).addParameter("limit", "100")
            .addParameter("sort", "top")
            .addParameter("depth", "2")
            .build();
    String json = request(uri);
  
    JsonArray root = gson.fromJson(json, JsonArray.class);
    JsonArray comments =
            root.get(1).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("children");

    Iterator<JsonElement> iterator = comments.iterator();
    List<JsonElement> elements = new ArrayList<>();
    iterator.forEachRemaining(elements::add);

    return elements.stream()
            .map(JsonElement::getAsJsonObject)
            .filter(o -> o.get("kind").getAsString().equals("t1"))
            .map(o -> o.getAsJsonObject("data"))
            .filter(o -> o.has("author"))
            .filter(o -> !"[deleted]".equals(o.get("author").getAsString()))
            .filter(o -> !"[removed]".equals(o.get("body").getAsString()))
            .map(o -> makeComment(o, subreddit))
            // TODO: 9/12/2019 add better sort alg numbers > score, date recent, 
            .sorted((c1, c2) -> Integer.compare(c2.getScore(), c1.getScore()))
            .collect(Collectors.toList());
  }

  @SneakyThrows
  public Subreddit getSubreddit(String name) {
    String endpoint = String.format("https://www.reddit.com/r/%s/about.json", name);
    URI uri =
            new URIBuilder(endpoint).addParameter("sort", "top").addParameter("depth", "2").build();
    String jsonString = request(uri);
  
    JsonObject root = gson.fromJson(jsonString, JsonObject.class);
  
    JsonObject data = root.getAsJsonObject("data");
    String displayName = data.get("display_name").getAsString();
    String displayNamePrefixed = data.get("display_name_prefixed").getAsString();
    String icon = data.get("icon_img").getAsString();
    var builder = Subreddit.builder();
    builder.name(name);
    builder.displayName(displayName);
    builder.displayNamePrefixed(displayNamePrefixed);
    builder.iconUrl(icon);
    return builder.build();
  }
  
  private RedditComment makeComment(JsonObject data, Subreddit subreddit) {
    String id = data.get("id").getAsString();
    String author = data.get("author").getAsString();
    int score = data.get("score").getAsInt();
    String content = data.get("body_html").getAsString();
    content = Parser.unescapeEntities(content, true);
    String link = String.format("https://www.reddit.com%s", data.get("permalink").getAsString());
    return new RedditComment(subreddit, author, score, content, Collections.emptyList(), id, link);
  }
  
  @SneakyThrows
  @Retryable(value = IOException.class, backoff = @Backoff(delay = 5000, multiplier = 2.5))
  private String request(URI uri) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }
  
  public enum Sort {
    TOP,
    HOT
  }
  
  public enum Time {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR,
    ALL
  }
}
