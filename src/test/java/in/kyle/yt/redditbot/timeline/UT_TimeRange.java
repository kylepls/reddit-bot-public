package in.kyle.yt.redditbot.timeline;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UT_TimeRange {

  @Test
  void getOverlap() {
    TimeRange r1 = new TimeRange(new Timestamp(0), new Timestamp(10));
    TimeRange r2 = new TimeRange(new Timestamp(5), new Timestamp(15));
    TimeRange overlap = r1.getOverlap(r2).get();
    assertThat(overlap).isEqualTo(new TimeRange(new Timestamp(5), new Timestamp(10)));
  }

  @Test
  void testContains() {
    TimeRange r1 = new TimeRange(new Timestamp(0), new Timestamp(10));
    assertThat(r1.contains(new Timestamp(5))).isTrue();
  }
}
