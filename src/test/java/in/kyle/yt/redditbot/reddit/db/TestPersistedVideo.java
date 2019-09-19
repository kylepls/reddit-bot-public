package in.kyle.yt.redditbot.reddit.db;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.function.Supplier;

import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import in.kyle.yt.redditbot.reddit.model.TestRedditCommment;
import in.kyle.yt.redditbot.reddit.model.TestRedditThread;
import in.kyle.yt.redditbot.utils.Make;

public class TestPersistedVideo {

  public static PersistedVideo newPersistedVideo() {
    Faker faker = new Faker();
    RedditThread thread = TestRedditThread.newRedditThread();
    List<RedditComment> comments =
        Make.make(5, (Supplier<RedditComment>) TestRedditCommment::newComment);
    List<PersistableSong> songs = Make.make(5, TestPersistableSong::newSong);
    List<String> tags = Make.make(5, faker.esports()::game);
    return new PersistedVideo(
        thread, comments, songs, tags, faker.book().title(), faker.rickAndMorty().quote());
  }
}
