package in.kyle.yt.redditbot.timeline;

import java.awt.*;
import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.timeline.filter.image.DimensionFilter;
import in.kyle.yt.redditbot.timeline.media.Media;
import in.kyle.yt.redditbot.timeline.media.TestImageMedia;
import in.kyle.yt.redditbot.timeline.media.TestSoundMedia;
import in.kyle.yt.redditbot.timeline.media.TestVideoMedia;
import in.kyle.yt.redditbot.timeline.modifier.AnimationModifier;
import in.kyle.yt.redditbot.utils.Make;
import in.kyle.yt.redditbot.utils.Resources;

public class TestTimeline {

  private static final Path directory = Resources.copyResources(TestTimeline.class);

  public static Timeline newTimeline() {
    Timeline timeline = new Timeline(new Dimension(1920, 1080), Color.GRAY);

    String[] colors = {"red", "green", "blue", "black"};
    String[][] positions = {{"0", "0"}, {"W/2", "0"}, {"0", "H/2"}, {"W/2", "H/2"}};
    List<Track> colorsTracks = Make.make(5, i -> timeline.makeTrack("Color Track " + i));
    for (int i = 0; i < 4; i++) {
      Track track = colorsTracks.get(i);
      Media media = TestImageMedia.newImage(directory.resolve(colors[i] + ".jpg"));
      media.setDuration(new Duration(5000));
      VisualTrackElement element = track.put(Timestamp.ZERO, media);
      element.setPosition(positions[i][0], positions[i][1], true);
      element.addFilter(new DimensionFilter("iw/2", "ih/2"));
    }

    Track whiteTrack = timeline.makeTopTrack("white");
    Media media = TestImageMedia.newImage(directory.resolve("white.jpg"));
    media.setDuration(new Duration(4000));
    VisualTrackElement element = whiteTrack.put(Timestamp.ZERO, media);
    element.addFilter(new DimensionFilter("iw/4", "ih/4"));
    AnimationModifier modifier = new AnimationModifier();
    modifier.setFrom("0", "0");
    modifier.setTo("W", "H");
    modifier.apply(element);

    Track videoTrack = timeline.makeTopTrack("video");
    Media video = TestVideoMedia.newVideo(directory.resolve("video.mp4"));
    video.setDuration(new Duration(5000));
    element = videoTrack.put(new Timestamp(1000), video);
    element.addFilter(new DimensionFilter("iw/4", "ih/4"));
    element.setPosition("W/2-w", "H/2-h", true);

    Track soundTrack = timeline.makeTopTrack("sound");
    Media sound = TestSoundMedia.newSound(directory.resolve("sound.mp3"));
    sound.setDuration(new Duration(5000));
    soundTrack.put(new Timestamp(0), sound);

    return timeline;
  }
}
