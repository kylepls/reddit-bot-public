package in.kyle.yt.redditbot.util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.file.Path;

@Component
class GsonPathAdapter implements GsonAdapter<Path> {

  @Override
  public Path deserialize(JsonElement element, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    return Path.of(element.getAsString());
  }

  @Override
  public JsonElement serialize(Path path, Type type, JsonSerializationContext context) {
    return context.serialize(path.toString());
  }

  @Override
  public Class<Path> getType() {
    return Path.class;
  }
}
