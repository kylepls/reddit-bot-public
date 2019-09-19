package in.kyle.yt.redditbot.reddit.model;

import com.github.javafaker.Faker;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestRedditAward {

  public static final RedditAward GOLD =
      new RedditAward(1, "https://www.redditstatic.com/gold/awards/icon/silver_512.png");
  public static final RedditAward SILVER =
      new RedditAward(1, "https://www.redditstatic.com/gold/awards/icon/gold_512.png");
  public static final RedditAward PLATINUM =
      new RedditAward(1, "https://www.redditstatic.com/gold/awards/icon/platinum_512.png");

  public static RedditAward newRedditAward() {
    Faker faker = new Faker();
    return new RedditAward(faker.random().nextInt(10), faker.internet().image());
  }
}
