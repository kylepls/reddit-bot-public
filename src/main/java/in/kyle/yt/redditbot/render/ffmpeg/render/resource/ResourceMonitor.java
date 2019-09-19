package in.kyle.yt.redditbot.render.ffmpeg.render.resource;

import lombok.Value;

public interface ResourceMonitor {

  ResourceUsage getUsage(long pid);

  @Value
  class ResourceUsage {
    String cpu;
    String memory;
  }
}
