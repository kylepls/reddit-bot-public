package in.kyle.yt.redditbot.timeline.modifier;

import in.kyle.yt.redditbot.timeline.TrackElement;

/** Modifiers change TrackElements to produce effects */
public interface Modifier<T extends TrackElement> {

  void apply(T t);
}
