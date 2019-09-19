package in.kyle.yt.redditbot.keyword.cortical;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

import in.kyle.yt.redditbot.keyword.KeywordExtractor;
import io.cortical.retina.client.LiteClient;
import lombok.SneakyThrows;

@Service
class CorticalNlp implements KeywordExtractor {

  private static final long COOLDOWN = 7000;
  private final LiteClient lite;
  private long lastRequest;

  public CorticalNlp(CorticalSettings settings) {
    this.lite = new LiteClient(settings.getApiKey());
    this.lastRequest = System.currentTimeMillis() - COOLDOWN;
  }

  @Override
  @SneakyThrows
  @Retryable(value = NullPointerException.class, backoff = @Backoff(delay = 5000, multiplier = 2.5))
  @Cacheable("keywords")
  public List<String> getKeywords(String text) {
    waitForCooldown();
    return lite.getKeywords(text);
  }

  private void waitForCooldown() throws InterruptedException {
    // the api locks up if requests are executed too quickly
    long timeWaited = System.currentTimeMillis() - lastRequest;
    long wait = COOLDOWN - timeWaited;
    if (wait > 0) {
      Thread.sleep(wait);
    }
    lastRequest = System.currentTimeMillis();
  }
}
