package in.kyle.yt.redditbot.tts.vocalware;

import org.apache.http.client.utils.URIBuilder;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.List;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.tts.TtsEngine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

//@Service
@RequiredArgsConstructor
class VocalwareTts implements TtsEngine {

  private final HttpClient client = HttpClient.newHttpClient();

  private final VocalwareTtsSettings settings;

  @Override
  @SneakyThrows
  public void generateSpeech(String input, Path output, float speed) {
    Conditions.error("Speed not supported");
    URI uri = makeUri(input);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
    byte[] body = response.body();
    Files.write(output, body);
  }

  @SneakyThrows
  private URI makeUri(String input) {
    String baseUrl = "http://www.vocalware.com/tts/gen.php";
    String EID = settings.getEid();
    String LID = settings.getLid();
    String VID = settings.getVid();
    String ACC = settings.getAcc();
    String API = settings.getApi();
    String phrase = settings.getSecretPhrase();
    return new URIBuilder(baseUrl)
        .addParameter("EID", EID)
        .addParameter("LID", LID)
        .addParameter("VID", VID)
        .addParameter("ACC", ACC)
        .addParameter("API", API)
        .addParameter("TXT", input)
        .addParameter("CS", md5(EID, LID, VID, input, ACC, API, phrase))
        .build();
  }

  @SneakyThrows
  private String md5(String... input) {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    String inputString = String.join("", List.of(input));
    byte[] bytes = digest.digest(inputString.getBytes());
    BigInteger no = new BigInteger(1, bytes);

    StringBuilder hashText = new StringBuilder(no.toString(16));
    while (hashText.length() < 32) {
      hashText.insert(0, "0");
    }

    return hashText.toString();
  }
}
