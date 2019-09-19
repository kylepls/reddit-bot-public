package in.kyle.yt.redditbot.util.retry;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
class RetryLogger {
  @Bean
  public List<RetryListener> retryListeners() {
    return Collections.singletonList(
        new RetryListenerSupport() {
          @Override
          public <T, E extends Throwable> void onError(
              RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
            log.warn(
                "Retryable method {} threw {}th exception {}",
                context.getAttribute("context.name"),
                context.getRetryCount(),
                throwable.toString());
          }
        });
  }
}
