package in.kyle.yt.redditbot.uploaders.youtube;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class YoutubeUploadReporter implements MediaHttpUploaderProgressListener {

  private final Map<UploadState, Function<MediaHttpUploader, String>> consumers = new HashMap<>();

  YoutubeUploadReporter() {
    consumers.put(UploadState.INITIATION_STARTED, m -> "Initializing upload...");
    consumers.put(UploadState.INITIATION_COMPLETE, m -> "Initialized upload");
    consumers.put(UploadState.MEDIA_IN_PROGRESS, this::reportProgress);
    consumers.put(UploadState.MEDIA_COMPLETE, m -> "Upload completed");
  }

  @SneakyThrows
  private String reportProgress(MediaHttpUploader uploader) {
    return String.format("Upload progress: %.2f", uploader.getProgress() * 100) + "%";
  }

  @Override
  public void progressChanged(MediaHttpUploader uploader) {
    UploadState state = uploader.getUploadState();
    String result = consumers.getOrDefault(state, i -> "").apply(uploader);
    if (!result.isEmpty()) {
      log.info(result);
    }
  }
}
