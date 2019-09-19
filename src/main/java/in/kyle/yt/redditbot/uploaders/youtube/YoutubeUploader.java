package in.kyle.yt.redditbot.uploaders.youtube;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Thumbnails;
import com.google.api.services.youtube.model.*;

import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.uploaders.UploadResult;
import in.kyle.yt.redditbot.uploaders.Uploader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Slf4j
@Service
@RequiredArgsConstructor
class YoutubeUploader implements Uploader {
  
  private final YoutubeSettings settings;
  private final YouTube youtube;
  
  @SneakyThrows
  public UploadResult upload(in.kyle.yt.redditbot.uploaders.Video metaSource) {
    Video returnedVideo = uploadVideo(metaSource);
    String videoId = returnedVideo.getId();
    var thumbnail = uploadThumbnail(videoId, metaSource.getThumbnailFile());
    likeVideo(videoId);
    addFirstComment(videoId, metaSource.getComment());
    return new YoutubeUploadResult(returnedVideo, thumbnail);
  }
  
  @Retryable(value = IOException.class, backoff = @Backoff(delay = 20000, multiplier = 2.5))
  private Video uploadVideo(in.kyle.yt.redditbot.uploaders.Video metaSource) throws IOException {
    YouTube.Videos.Insert insert = createVideoMeta(metaSource);
    
    var uploader = insert.getMediaHttpUploader();
    uploader.setDirectUploadEnabled(false);
    uploader.setProgressListener(new YoutubeUploadReporter());
    
    return insert.execute();
  }
  
  @Retryable(value = IOException.class, backoff = @Backoff(delay = 20000, multiplier = 2.5))
  private ThumbnailSetResponse uploadThumbnail(String videoId, Path image) throws IOException {
    BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(image));
    InputStreamContent mediaContent = new InputStreamContent(YoutubeFormats.IMAGE_PNG, inputStream);
    mediaContent.setLength(Files.size(image));
    Thumbnails.Set thumbnailSet = youtube.thumbnails().set(videoId, mediaContent);
    
    var uploader = thumbnailSet.getMediaHttpUploader();
    uploader.setDirectUploadEnabled(false);
    uploader.setProgressListener(new YoutubeUploadReporter());
    
    return thumbnailSet.execute();
  }
  
  private YouTube.Videos.Insert createVideoMeta(in.kyle.yt.redditbot.uploaders.Video metaSource)
          throws IOException {
    Video metaTarget = new Video();
    
    var status = new VideoStatus();
    status.setPrivacyStatus(YoutubePrivacyStatus.PUBLIC.getApiValue());
    metaTarget.setStatus(status);
    
    var videoRecordingDetails = new VideoRecordingDetails();
    videoRecordingDetails.setLocationDescription("Reddit");
    metaTarget.setRecordingDetails(videoRecordingDetails);
    
    //    var fileDetails = new VideoFileDetails();
    //    fileDetails.setFileName(String.join("-", metaSource.getKeywords()) + ".mp4");
    //    metaTarget.setFileDetails(fileDetails);
    
    var snippet = new VideoSnippet();
    snippet.setCategoryId(settings.getCategoryId());
    Conditions.isTrue(metaSource.getTitle().length() <= 100,
                      "Title too long {}:{}",
                      metaSource.getTitle(),
                      metaSource.getTitle().length());
    snippet.setTitle(metaSource.getTitle());
    snippet.setDescription(metaSource.getDescription());
    snippet.setTags(metaSource.getTags());
    metaTarget.setSnippet(snippet);
    
    var media = new InputStreamContent(YoutubeFormats.VIDEO, metaSource.getVideoStream());
    media.setLength(metaSource.getVideoLength());
    
    // This is stupid...
    // https://developers.google.com/youtube/v3/docs/videos/list
    return youtube.videos().insert("snippet,statistics,status,recordingDetails", metaTarget, media);
  }
  
  void likeVideo(String videoId) throws IOException {
    var rateRequest = youtube.videos().rate(videoId, YoutubeRating.LIKE.getApiValue());
    rateRequest.execute();
  }
  
  void addFirstComment(String videoId, String commentText) throws IOException {
    var commentSnippet = new CommentSnippet();
    commentSnippet.setTextOriginal(commentText);
    commentSnippet.setViewerRating(YoutubeRating.LIKE.getApiValue());
    
    var comment = new Comment();
    comment.setSnippet(commentSnippet);
    
    var threadSnippet = new CommentThreadSnippet();
    threadSnippet.setTopLevelComment(comment);
    threadSnippet.setVideoId(videoId);
    
    var thread = new CommentThread();
    thread.setSnippet(threadSnippet);
    
    youtube.commentThreads().insert("snippet", thread).execute();
  }
}
