package in.kyle.yt.redditbot.task.impl.tasks;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.Set;

import in.kyle.yt.redditbot.render.RenderDirectory;
import in.kyle.yt.redditbot.task.impl.Task;
import in.kyle.yt.redditbot.task.impl.VideoGenContext;
import in.kyle.yt.redditbot.uploaders.UploadResult;
import in.kyle.yt.redditbot.uploaders.Video;
import in.kyle.yt.redditbot.uploaders.VideoUploader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(5)
class UploadTask implements Task {

  private final VideoUploader uploader;

  @Override
  public VideoGenContext process(VideoGenContext context) {
    Video video = makeVideo(context);
    Set<UploadResult> upload = uploader.upload(video);
    for (UploadResult uploadResult : upload) {
      log.info(uploadResult.getInfo());
    }
    return context;
  }

  @SneakyThrows
  private Video makeVideo(VideoGenContext context) {
    RenderDirectory directory = context.getDirectory();
    return Video.builder()
        .title(context.getTitle())
        .description(context.getDescription())
        .comment(context.getComment())
        .tags(context.getTags())
        .thumbnailFile(directory.getThumbnail())
        .videoStream(Files.newInputStream(directory.getOutputFile()))
        .videoLength(Files.size(directory.getOutputFile())).keywords(context.getKeywords())
        .build();
  }
}
