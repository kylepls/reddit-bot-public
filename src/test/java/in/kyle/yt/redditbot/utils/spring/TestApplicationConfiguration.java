package in.kyle.yt.redditbot.utils.spring;

import com.github.javafaker.Faker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import in.kyle.yt.redditbot.RedditBotApplication;

@Configuration
@Import(RedditBotApplication.class)
public class TestApplicationConfiguration {

  @Bean
  public Faker faker() {
    return new Faker();
  }
}
