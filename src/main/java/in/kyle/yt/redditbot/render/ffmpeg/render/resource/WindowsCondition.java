package in.kyle.yt.redditbot.render.ffmpeg.render.resource;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

class WindowsCondition implements Condition {
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    return SystemUtils.IS_OS_WINDOWS;
  }
}
