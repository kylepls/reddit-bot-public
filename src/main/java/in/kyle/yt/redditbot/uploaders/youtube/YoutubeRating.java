package in.kyle.yt.redditbot.uploaders.youtube;

public enum YoutubeRating {
  LIKE,
  DISLIKE,
  NONE;

  public String getApiValue() {
    return name().toLowerCase();
  }
}
