package in.kyle.yt.redditbot.imager;

import java.util.List;

public interface Imager {

  List<ImagerResult> execute(List<ImagerCommand> commands);
}
