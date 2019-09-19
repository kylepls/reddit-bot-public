package in.kyle.yt.redditbot.music;

import com.github.javafaker.Faker;

import in.kyle.yt.redditbot.timeline.TestDuration;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestSong {

  public static Song newSong() {
    Faker faker = new Faker();
    return new Song(
        "DJ " + faker.rickAndMorty().character(),
        "Dancing in the " + faker.rickAndMorty().location(),
        null,
        TestDuration.newDuration());
  }
}
