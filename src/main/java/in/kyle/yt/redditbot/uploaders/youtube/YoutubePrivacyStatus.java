package in.kyle.yt.redditbot.uploaders.youtube;

enum YoutubePrivacyStatus {
  UNLISTED,
  PUBLIC;

  public String getApiValue() {
    return name().toLowerCase();
  }
}
