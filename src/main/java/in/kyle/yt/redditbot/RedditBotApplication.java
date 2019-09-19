package in.kyle.yt.redditbot;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import in.kyle.yt.redditbot.task.RedditGenerateVideoTask;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableRetry
@EnableCaching
@EnableMongoRepositories
@PropertySource("classpath:config.properties")
public class RedditBotApplication {
  
  public static void main(String[] args) {
    var ctx = SpringApplication.run(RedditBotApplication.class, args);
    RedditGenerateVideoTask taskDispatcher = ctx.getBean(RedditGenerateVideoTask.class);
    long start = System.currentTimeMillis();
    taskDispatcher.runGeneration();
    long end = System.currentTimeMillis();
    String time = DurationFormatUtils.formatDurationHMS(end - start);
    log.info("Full video generation completed in {}", time);
  }
  
  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }
  
  @Bean
  public Validator validator() {
    return new LocalValidatorFactoryBean();
  }
}
