package in.kyle.yt.redditbot.reddit.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

interface RedditVideoRepository extends MongoRepository<PersistedVideo, String> {

  @Query("{ 'thread.identifier' : ?0 }")
  PersistedVideo findDistinctFirstByThreadId(String id);
}
