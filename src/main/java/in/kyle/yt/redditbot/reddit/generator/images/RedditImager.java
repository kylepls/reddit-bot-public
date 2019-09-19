package in.kyle.yt.redditbot.reddit.generator.images;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import in.kyle.api.utils.Conditions;
import in.kyle.yt.redditbot.imager.Imager;
import in.kyle.yt.redditbot.imager.ImagerCommand;
import in.kyle.yt.redditbot.imager.ImagerResult;
import in.kyle.yt.redditbot.reddit.model.RedditComment;
import in.kyle.yt.redditbot.reddit.model.RedditThread;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedditImager {

  private final RedditImagerSettings settings;
  private final Imager imager;

  @SneakyThrows
  public RedditContextTextParts generateImages(
      Path outputDir, RedditThread thread, List<RedditComment> comments) {
    Conditions.isTrue(!thread.isOver18(), "No support for >18 posts");

    var builder = RedditContextTextParts.builder();

    var titleCommand = makeTitleCommand(outputDir, thread);
    ImagerResult titleResult = singularExecution(titleCommand);
    builder.titleParts(titleResult.getParts());

    log.info("Generating comment images...");
    comments
        .parallelStream()
        .map(comment -> getResultForComment(comment, outputDir))
        .collect(Collectors.toList())
        .forEach(c -> builder.commentPart(c.getComment(), c.getResult().getParts()));
    log.info("Generated images for {} comments", comments.size());

    return builder.build();
  }

  private WrappedImagerResult getResultForComment(RedditComment comment, Path outputDir) {
    var commentCommand = makeCommentCommand(outputDir, comment);
    ImagerResult commentResult = singularExecution(commentCommand);
    commentResult.getOutputFiles().forEach(this::resize);
    return new WrappedImagerResult(comment, commentResult);
  }

  @SneakyThrows
  private void resize(Path file) {
    BufferedImage original = ImageIO.read(Files.newInputStream(file));
    int width = 1600;
    int height = (width * original.getHeight()) / original.getWidth();
    BufferedImage resized = new BufferedImage(width, height, original.getType());
    Graphics2D g = resized.createGraphics();
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(
        original, 0, 0, width, height, 0, 0, original.getWidth(), original.getHeight(), null);
    g.dispose();
    ImageIO.write(resized, "png", Files.newOutputStream(file));
  }

  private ImagerResult singularExecution(ImagerCommand command) {
    log.info("Executing {}", command.getUrl());
    List<ImagerResult> execute = imager.execute(List.of(command));
    execute.stream().flatMap(e -> e.getOutputFiles().stream()).forEach(this::resize);
    Conditions.isTrue(execute.size() == 1, "Incorrect return arity {}", execute.size());
    return execute.get(0);
  }

  @SneakyThrows
  ImagerCommand makeTitleCommand(Path outputDir, RedditThread thread) {
    var builder = ImagerCommand.builder();
    builder.url(thread.getLink());
    builder.outputFilePrefix(outputDir.resolve("title"));
    builder.parentCssSelector(String.format("#t3_%s > div", thread.getIdentifier()));
    builder.textCssSelector(String.format("#t3_%s h1", thread.getIdentifier()));
    builder.payload(IOUtils.toString(settings.getPayloadFile().getInputStream()));
    builder.incremental(true);
    return builder.build();
  }

  @SneakyThrows
  ImagerCommand makeCommentCommand(Path outputDir, RedditComment comment) {
    var builder = ImagerCommand.builder();
    builder.url(comment.getLink());
    builder.outputFilePrefix(outputDir.resolve(comment.getIdentifier()));
    builder.parentCssSelector(String.format("#t1_%s", comment.getIdentifier()));
    builder.textCssSelector(String.format("#t1_%s p", comment.getIdentifier()));
    builder.payload(IOUtils.toString(settings.getPayloadFile().getInputStream()));
    builder.incremental(true);
    return builder.build();
  }

  @Value
  private static class WrappedImagerResult {
    RedditComment comment;
    ImagerResult result;
  }
}
