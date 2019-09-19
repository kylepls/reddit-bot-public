package in.kyle.yt.redditbot.uploaders.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
class YoutubeFactory {

  private final YoutubeSettings settings;

  private final HttpTransport httpTransport = new NetHttpTransport();
  private final JsonFactory jsonFactory = new JacksonFactory();

  @Bean
  public YouTube youTube() {
    Credential credential = getCredential();
    return new YouTube.Builder(httpTransport, jsonFactory, credential).build();
  }

  @SneakyThrows
  private Credential getCredential() {
    GoogleCredential credential =
        new GoogleCredential.Builder()
            .setClientSecrets(settings.getClientId(), settings.getClientSecret())
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .build();
    credential.setRefreshToken(settings.getRefreshToken());
    credential.refreshToken();
    return credential;
  }
}
