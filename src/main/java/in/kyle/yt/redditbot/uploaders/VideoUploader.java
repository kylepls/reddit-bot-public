package in.kyle.yt.redditbot.uploaders;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoUploader {

  private final Set<Uploader> uploaders;

  public Set<UploadResult> upload(Video video) {
    return uploaders.stream().map(uploader -> uploader.upload(video)).collect(Collectors.toSet());
  }
}
