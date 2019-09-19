package in.kyle.yt.redditbot.reddit.model;

import com.github.javafaker.Faker;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestSubreddit {

  public static Subreddit newSubreddit() {
    Faker faker = new Faker();
    String name = faker.funnyName().name();
    return new Subreddit(name, name, String.format("r/%s", name), faker.internet().image());
  }
}
