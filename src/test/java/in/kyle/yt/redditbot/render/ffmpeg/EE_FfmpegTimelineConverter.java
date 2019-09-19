package in.kyle.yt.redditbot.render.ffmpeg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.nio.file.Path;

import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.render.TestRenderDirectory;
import in.kyle.yt.redditbot.render.TimelineRenderer;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.*;
import in.kyle.yt.redditbot.timeline.filter.image.FlipFilter;
import in.kyle.yt.redditbot.timeline.filter.image.GridFilter;
import in.kyle.yt.redditbot.timeline.filter.sound.VolumeFilter;
import in.kyle.yt.redditbot.timeline.media.ImageMedia;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.timeline.media.SoundMedia;
import in.kyle.yt.redditbot.timeline.media.VideoMedia;
import in.kyle.yt.redditbot.timeline.render.TimelineVisualizer;

@SpringBootTest
class EE_FfmpegTimelineConverter {

  @Autowired TimelineRenderer generator;
  @Autowired MediaFactory mediaFactory;
  @Autowired TimelineVisualizer visualizer;

  private RenderDirectory directory;
  private Timeline timeline;

  @BeforeEach
  void setup() {
    directory = TestRenderDirectory.newDirectoryWithResources(getClass());

    timeline = new Timeline(new Dimension(1920, 1080), Color.RED);
    Track top = timeline.makeTrack("Test");
    Track track = timeline.makeTrack("Test");

    Path imagesDir = directory.getImages();
    for (int i = 2; i < 5; i++) {
      Path file = imagesDir.resolve(String.format("dank%d.jpg", i));
      ImageMedia media = mediaFactory.load(file);
      media.setDuration(new Duration(2999));
      Timestamp start = new Timestamp(i * 3000 + 1);
      track.put(start, media);
    }

    VideoMedia media = mediaFactory.load(imagesDir.resolve("vid.mp4"));
    TrackElement added = track.put(new Timestamp(0), media);
    added.addFilter(new FlipFilter(FlipFilter.Type.VERTICAL));
    added.addFilter(new FlipFilter(FlipFilter.Type.HORIZONTAL));
    VolumeFilter filter = new VolumeFilter("2.0");
    added.addFilter(filter);

    Track audio = timeline.makeTrack("Audio");
    SoundMedia thx = mediaFactory.load(directory.getSounds().resolve("thx.mp3"));
    thx.setDuration(new Duration(5000));
    audio.put(new Timestamp(5000), thx);

    ImageMedia image = mediaFactory.load(imagesDir.resolve("dank1.jpg"));
    image.setDuration(new Duration(5000));
    VisualTrackElement put = top.put(new Timestamp(2000), image);
    put.setPosition(200, 200);
    GridFilter gridFilter = new GridFilter();
    put.addFilter(gridFilter);
  }

  @Test
  void test() {
    Timeline timeline = TestTimeline.newTimeline();
    generator.render(timeline, directory.getOutputFile());
    //    generator.render(this.timeline, directory.getOutputFile());
    System.out.println(directory.getOutputFile());
  }
}
