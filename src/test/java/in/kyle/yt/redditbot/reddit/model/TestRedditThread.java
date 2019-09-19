package in.kyle.yt.redditbot.reddit.model;

import com.github.javafaker.Faker;

import in.kyle.yt.redditbot.utils.Make;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestRedditThread {

  public static RedditThread newRedditThread() {
    return newRedditThread(TestSubreddit.newSubreddit());
  }

  public static RedditThread newRedditThread(Subreddit subreddit) {
    Faker faker = new Faker();
    return RedditThread.builder()
        .content(faker.book().title())
        .author(faker.rickAndMorty().character())
        .subreddit(subreddit)
        .awards(Make.make(2, TestRedditAward::newRedditAward))
        .identifier(faker.idNumber().valid())
        .link(faker.internet().url())
        .over18(faker.random().nextBoolean())
        .score(faker.random().nextInt(10000))
        .build();
  }
}
