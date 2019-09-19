package in.kyle.yt.redditbot.reddit.generator.video;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.timeline.Dimension;
import in.kyle.yt.redditbot.timeline.Duration;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import in.kyle.yt.redditbot.timeline.filter.sound.VolumeFilter;
import in.kyle.yt.redditbot.timeline.media.ImageMedia;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.timeline.media.SoundMedia;
import in.kyle.yt.redditbot.timeline.media.VideoMedia;
import in.kyle.yt.redditbot.timeline.media.VisualMedia;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class RedditTimelineGenerator {

  private final RedditVideoSettings videoSettings;
  private final MediaFactory mediaFactory;

  public Timeline generateTimeline(RenderDirectory directory, List<RedditComment> comments) {
    Dimension dimension = videoSettings.getDimension();
    Color bgColor = videoSettings.getBackgroundColor();
    Timeline timeline = new Timeline(dimension, bgColor);
    Track visualTrack = timeline.makeTrack("Images/Transitions");
    Track soundTrack = timeline.makeTrack("TTS/Sound");

    addIntro(visualTrack);
    renderSequence(directory, "title", visualTrack, soundTrack);
    addTransition(visualTrack);

    for (RedditComment comment : comments) {
      renderSequence(directory, comment.getIdentifier(), visualTrack, soundTrack);
      addTransition(visualTrack);
    }

    Duration minDuration = videoSettings.getMinTime();
    Timestamp end = visualTrack.getEnd();
    //    Conditions.isTrue(end.gt(minDuration), "Video not long enough: {} < {}", end,
    // minDuration);

    addOutro(visualTrack);

    return timeline;
  }

  private void renderSequence(
      RenderDirectory directory, String name, Track visualTrack, Track soundTrack) {
    int size = getSegmentCount(directory, name);
    List<PathPair> fileSequence = getFileSequence(directory, name, size);

    PathPair lastPair = fileSequence.get(fileSequence.size() - 1);
    ImageMedia last = mediaFactory.load(lastPair.getImage());
    int yPos = visualTrack.getDimension().getHeight() / 2 - last.getDimension().getHeight() / 2;

    fileSequence.forEach(
        pair -> writeNext(visualTrack, soundTrack, pair.getImage(), pair.getSound(), yPos));
  }

  private int getSegmentCount(RenderDirectory directory, String filename) {
    int i = 0;
    Path soundFile;
    Path imageFile;
    do {
      i++;
      imageFile = directory.getImages().resolve(String.format("%s%d.png", filename, i));
      soundFile = directory.getSounds().resolve(String.format("%s%d.mp3", filename, i));
      boolean imageExists = Files.exists(imageFile);
      boolean soundExists = Files.exists(soundFile);
      if (imageExists != soundExists) {
        Conditions.error(
            "Missing file for pair {}{}\n" + "sound exists = {} ({})\n" + "image exists = {} ({})",
            filename,
            i,
            soundExists,
            soundFile,
            imageExists,
            imageFile);
      }
    } while (Files.exists(soundFile));
    if (i == 1 && !Files.exists(soundFile)) {
      Conditions.error("File pair {}{} not found", filename, i);
    }
    return i - 1;
  }

  private void writeNext(
      Track visualTrack, Track audioTrack, Path imageFile, Path soundFile, int yPos) {
    VisualMedia image = mediaFactory.load(imageFile);
    SoundMedia tts = mediaFactory.load(soundFile);
    image.setDuration(tts.getDuration());

    Timestamp ts = visualTrack.getEnd().add(1);
    VisualTrackElement visualElement = visualTrack.put(ts, image);
      visualElement.setPosition("W/2-w/2", String.valueOf(yPos), true);
    audioTrack.put(ts, tts);
  }

  private List<PathPair> getFileSequence(RenderDirectory directory, String name, int count) {
    return IntStream.rangeClosed(1, count)
        .mapToObj(
            i -> {
              Path png = directory.getImages().resolve(name + i + ".png");
              Path mp3 = directory.getSounds().resolve(name + i + ".mp3");
              return new PathPair(png, mp3);
            })
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private void addTransition(Track track) {
    if (videoSettings.getTransition().exists()) {
      Path transitionFile = videoSettings.getTransition().getFile().toPath();
      var transition = addMedia(track, transitionFile);
      transition.addFilter(new VolumeFilter("0.5"));
    }
  }

  @SneakyThrows
  private void addIntro(Track track) {
    if (videoSettings.getIntro().exists()) {
      Path introFile = videoSettings.getIntro().getFile().toPath();
      addMedia(track, introFile);
    }
  }

  @SneakyThrows
  private void addOutro(Track track) {
    if (videoSettings.getOutro().exists()) {
      Path outroFile = videoSettings.getOutro().getFile().toPath();
      addMedia(track, outroFile);
    }
  }

  private VisualTrackElement addMedia(Track track, Path file) {
    VideoMedia transition = mediaFactory.load(file);
    return track.put(track.getEnd().add(1), transition);
  }

  @Data
  private static class PathPair {
    private final Path image;
    private final Path sound;
  }
}
