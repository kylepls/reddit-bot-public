package in.kyle.yt.redditbot.tts.polly;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClient;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AmazonPollyFactory {

  private final AmazonAuthSettings authSettings;

  @Bean
  public AmazonPolly amazonPolly() {
    BasicAWSCredentials credentials =
        new BasicAWSCredentials(authSettings.getKey(), authSettings.getSecret());
    return new AmazonPollyClient(credentials);
  }
}
