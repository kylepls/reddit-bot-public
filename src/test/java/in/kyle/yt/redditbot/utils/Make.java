package in.kyle.yt.redditbot.utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Make {

  public static <T> List<T> make(int count, Supplier<T> supplier) {
    return IntStream.range(0, count).mapToObj(i -> supplier.get()).collect(Collectors.toList());
  }

  public static <T> List<T> make(int count, Function<Integer, T> supplier) {
    return IntStream.range(0, count).mapToObj(supplier::apply).collect(Collectors.toList());
  }
}
