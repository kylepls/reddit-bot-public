package in.kyle.yt.redditbot.utils.spring;

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@Import(EmbeddedMongoAutoConfiguration.class)
class EmbeddedMongoConfiguration {}
