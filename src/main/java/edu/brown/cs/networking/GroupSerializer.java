package edu.brown.cs.networking;

import java.lang.reflect.Type;
import java.util.Collection;

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
    j.addProperty("groupName", src.groupName());
    Collection<User> users = src.connectedUsers();
    j.add("connectedUsers", Networking.GSON.toJsonTree(users));
    return j;
  }

}
