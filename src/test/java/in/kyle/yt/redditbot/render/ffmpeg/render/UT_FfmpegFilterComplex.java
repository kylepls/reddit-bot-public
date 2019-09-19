package in.kyle.yt.redditbot.render.ffmpeg.render;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.List;

import in.kyle.yt.redditbot.timeline.TestTrackElement;
import in.kyle.yt.redditbot.timeline.filter.model.Filter;
import in.kyle.yt.redditbot.utils.Make;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import static org.assertj.core.api.Assertions.assertThat;

class UT_FfmpegFilterComplex {

  private FfmpegFilterComplex ffmpegFilterComplex = new FfmpegFilterComplex(List.of(), 1000);

  @Test
  void testMakeFilterString() {
    FfmpegFilter filter = FfmpegFilter.builder().name("name").build();
    assertThat(ffmpegFilterComplex.makeFilterString(filter)).isEqualTo("name");

    filter = FfmpegFilter.builder().name("name").arg(Pair.of("key1", "value1")).build();
    assertThat(ffmpegFilterComplex.makeFilterString(filter)).isEqualTo("name=key1=value1");

    filter =
        FfmpegFilter.builder()
            .name("name")
            .arg(Pair.of("key1", "value1"))
            .arg(Pair.of("key2", "value2"))
            .build();
    assertThat(ffmpegFilterComplex.makeFilterString(filter))
        .isEqualTo("name=key1=value1:key2=value2");
  }

  @Test
  void testMakeOverlayReductionTree() {
    var inputStreams = Make.make(2, this::makeStream);
    var overlayStreams = ffmpegFilterComplex.makeOverlayReductionTree(inputStreams);
    assertThat(overlayStreams).hasSize(2);
    FfmpegStream overlay1 = overlayStreams.get(0);
    assertThat(overlay1.getInputs()).containsOnly("0", "output");
    assertThat(overlay1.getOutput()).isEqualTo("o1");
    assertThat(overlay1.getFilters()).hasSize(1);

    FfmpegStream overlay2 = overlayStreams.get(1);
    assertThat(overlay2.getInputs()).containsOnly("o1", "output");
    assertThat(overlay2.getOutput()).isEqualTo("o2");
    assertThat(overlay2.getFilters()).hasSize(1);
  }

  private FfmpegStream makeStream() {
    return FfmpegStream.builder()
        .input("input")
        .filter(FfmpegFilter.builder().name("filter").build())
        .source(TestTrackElement.newVisualTrackElement())
        .output("output")
        .build();
  }

  @Value
  @Builder
  static class TestFilter implements Filter {

    @Singular List<Pair<String, String>> values;

    @Override
    public String getName() {
      return "name";
    }
  }
}
