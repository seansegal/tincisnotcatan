package edu.brown.cs.networking;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GroupSerializer implements JsonSerializer<Group> {

  @Override
  public JsonElement serialize(Group src, Type typeOfSrc,
      JsonSerializationContext context) {
    JsonObject j = new JsonObject();
    j.addProperty("id", src.identifier());
    j.addProperty("maxSize", src.maxSize());
    j.addProperty("currentSize", src.currentSize());
    return j;
  }

}
