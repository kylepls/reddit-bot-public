package in.kyle.yt.redditbot.reddit.model;

import com.github.javafaker.Faker;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestRedditCommment {

  public static RedditComment newComment(String id) {
    Faker faker = new Faker();
    return RedditComment.builder()
        .author(faker.rickAndMorty().character())
        .content(faker.rickAndMorty().quote())
        .identifier(id)
        .link(String.format("http://link.to/%s", id))
        .score(faker.random().nextInt(10000))
        .build();
  }

  public static RedditComment newComment() {
    return newComment(new Faker().idNumber().valid());
  }
}
