package in.kyle.yt.redditbot.music;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.timeline.Timeline;
import in.kyle.yt.redditbot.timeline.Timestamp;
import in.kyle.yt.redditbot.timeline.Track;
import in.kyle.yt.redditbot.timeline.TrackElement;
import in.kyle.yt.redditbot.timeline.VisualTrackElement;
import in.kyle.yt.redditbot.timeline.filter.sound.VolumeFilter;
import in.kyle.yt.redditbot.timeline.media.Media;
import in.kyle.yt.redditbot.timeline.media.MediaFactory;
import in.kyle.yt.redditbot.timeline.media.SoundMedia;
import in.kyle.yt.redditbot.timeline.modifier.AnimationModifier;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Component
@RequiredArgsConstructor
public class BackgroundMusicGenerator {

  private final MusicLibrary musicLibrary;
  private final MusicSettings settings;
  private final MusicCardGenerator cardGenerator;
  private final MediaFactory mediaFactory;

  @SneakyThrows
  public List<TimedSong> addBackgroundMusic(Timeline timeline, RenderDirectory directory) {
    List<TimedSong> songs = musicLibrary.makePlaylist(timeline.getMaxTime());

    Track cardTrack = timeline.makeTopTrack("Background Music: Cards");
    Track audioTrack = timeline.makeTrack("Background Music: Audio");

    for (TimedSong song : songs) {
      addSong(song, audioTrack);
      addCard(song, directory.getCards(), cardTrack);
    }

    return songs;
  }

  private void addSong(TimedSong timedSong, Track audioTrack) {
    SoundMedia media = musicLibrary.loadMedia(timedSong.getSong());
    media.setDuration(timedSong.getSong().getDuration());
    TrackElement element = audioTrack.put(timedSong.getStart(), media);
    element.addFilter(new VolumeFilter("0.125"));
  }

  private void addCard(TimedSong timedSong, Path cards, Track cardTrack) {
    Path imagePath = cards.resolve(timedSong.getSong().getFile().getFileName() + ".png");
    cardGenerator.makeImage(timedSong, imagePath);
    Timestamp songStart = timedSong.getStart();

    animateIn(cardTrack, songStart, imagePath);
    stay(cardTrack, songStart, imagePath);
    animateOut(cardTrack, songStart, imagePath);
  }

  private void animateIn(Track cardTrack, Timestamp songStart, Path imageFile) {
    Media card = mediaFactory.load(imageFile);
    card.setDuration(settings.getCardAnimateInTime().subtract(1));
    VisualTrackElement e = cardTrack.put(songStart, card);
    AnimationModifier animateIn = new AnimationModifier();
    animateIn.setY("H", "H-h");
    animateIn.setX("W-w", "W-w");
    animateIn.apply(e);
  }

  private void stay(Track cardTrack, Timestamp songStart, Path imageFile) {
    Media card = mediaFactory.load(imageFile);
    card.setDuration(settings.getCardIdleTime().subtract(1));
    VisualTrackElement e = cardTrack.put(songStart.add(settings.getCardAnimateInTime()), card);
    e.setPosition("W-w", "H-h", true);
  }

  private void animateOut(Track cardTrack, Timestamp songStart, Path imageFile) {
    Media card = mediaFactory.load(imageFile);
    card.setDuration(settings.getCardAnimateOutTime().subtract(1));
    Timestamp timestamp =
        songStart.add(settings.getCardAnimateInTime()).add(settings.getCardIdleTime());
    VisualTrackElement e = cardTrack.put(timestamp, card);
    AnimationModifier animateOut = new AnimationModifier();
    animateOut.setY("H-h", "H");
    animateOut.setX("W-w", "W-w");
    animateOut.apply(e);
  }
}
