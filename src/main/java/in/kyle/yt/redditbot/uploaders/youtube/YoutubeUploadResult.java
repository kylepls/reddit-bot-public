package in.kyle.yt.redditbot.uploaders.youtube;

import com.google.api.services.youtube.model.ThumbnailSetResponse;
import com.google.api.services.youtube.model.Video;

import in.kyle.yt.redditbot.uploaders.UploadResult;
import lombok.Value;

@Value
class YoutubeUploadResult implements UploadResult {

  Video video;
  ThumbnailSetResponse thumbnail;

  @Override
  public String getInfo() {
    StringBuilder builder = new StringBuilder();
    printlnf(builder, "Returned Video:");
    printlnf(builder, "  - Id: %s", video.getId());
    printlnf(builder, "  - URL: https://www.youtube.com/watch?v=%s", video.getId());
    printlnf(builder, "  - Title: %s", video.getSnippet().getTitle());
    printlnf(builder, "  - Tags: %s", video.getSnippet().getTags());
    printlnf(builder, "  - Privacy Status: %s", video.getStatus().getPrivacyStatus());
    printlnf(builder, "  - Thumbnail: %s", thumbnail.getItems().get(0).getDefault().getUrl());
    return builder.toString();
  }

  private void printlnf(StringBuilder builder, String format, Object... args) {
    builder.append(String.format(format, args)).append("\n");
  }
}
