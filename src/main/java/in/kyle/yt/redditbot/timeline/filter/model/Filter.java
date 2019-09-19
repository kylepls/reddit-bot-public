package in.kyle.yt.redditbot.timeline.filter.model;

import org.springframework.data.util.Pair;

import java.util.List;

public interface Filter {

  String getName();

  List<Pair<String, String>> getValues();
}
