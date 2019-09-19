package in.kyle.yt.redditbot.timeline;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import in.kyle.yt.redditbot.timeline.media.Media;

import static org.assertj.core.api.Assertions.assertThat;

class UT_Track {

  @Test
  void testGetNone() {
    Track track = new Track("Test", new Dimension(0, 0));
    TrackElement media = track.get(new Timestamp(0));
    assertThat(media).isNull();
  }

  @Test
  void testTimeRanges() {
    Track track = new Track("Test", new Dimension(0, 0));
    track.put(new Timestamp(0), TestMedia.newMedia(1000));

    LinkedHashMap<TimeRange, TrackElement> timeRanges = track.getTimeRanges();

    var iterator = timeRanges.entrySet().iterator();
    var firstRange = iterator.next();
    var secondRange = iterator.next();

    TimeRange rangeFirst = firstRange.getKey();
    assertThat(rangeFirst.getStart()).isEqualTo(Timestamp.ZERO);
    assertThat(rangeFirst.getEnd()).isEqualTo(new Timestamp(1000));
    assertThat(firstRange.getValue()).isNotNull();

    TimeRange rangeSecond = secondRange.getKey();
    assertThat(rangeSecond.getStart()).isEqualTo(new Timestamp(1001));
    assertThat(rangeSecond.getEnd()).isEqualTo(Timestamp.MAX);
    assertThat(secondRange.getValue()).isNull();
  }

  @Test
  void testTimeRangeJump() {
    Track track = new Track("Test", new Dimension(0, 0));
    track.put(new Timestamp(1000), TestMedia.newMedia(1000));

    LinkedHashMap<TimeRange, TrackElement> timeRanges = track.getTimeRanges();

    var iterator = timeRanges.entrySet().iterator();
    var firstRange = iterator.next();
    var secondRange = iterator.next();
    var thirdRange = iterator.next();

    TimeRange rangeFirst = firstRange.getKey();
    assertThat(rangeFirst.getStart()).isEqualTo(Timestamp.ZERO);
    assertThat(rangeFirst.getEnd()).isEqualTo(new Timestamp(999));
    assertThat(firstRange.getValue()).isNull();

    TimeRange rangeSecond = secondRange.getKey();
    assertThat(rangeSecond.getStart()).isEqualTo(new Timestamp(1000));
    assertThat(rangeSecond.getEnd()).isEqualTo(new Timestamp(2000));
    assertThat(secondRange.getValue()).isNotNull();

    TimeRange rangeThird = thirdRange.getKey();
    assertThat(rangeThird.getStart()).isEqualTo(new Timestamp(2001));
    assertThat(rangeThird.getEnd()).isEqualTo(Timestamp.MAX);
    assertThat(thirdRange.getValue()).isNull();
  }

  @Test
  void testGetSingle() {
    Track track = new Track("Test", new Dimension(0, 0));
    Media media = TestMedia.newMedia(1000);
    track.put(new Timestamp(0), media);

    TrackElement put = track.get(new Timestamp(0));
    assertThat(put.getStart().getMillis()).isEqualTo(0);
    assertThat(put.getStart().getMillis()).isEqualTo(0);
    assertThat(put.getEnd().getMillis()).isEqualTo(1000);
    assertThat(put.getMedia()).isSameAs(media);
    assertThat(track.get(new Timestamp(500)).getMedia()).isSameAs(media);
  }

  @Test
  void testAddOrdered() {
    Track track = new Track("Test", new Dimension(0, 0));
    Media media = TestMedia.newMedia(999);
    track.put(new Timestamp(1), media);
    track.put(new Timestamp(1001), media);

    assertThat(track.getEnd().getMillis()).isEqualTo(2000);
  }
}
