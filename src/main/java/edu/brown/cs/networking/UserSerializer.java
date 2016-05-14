package edu.brown.cs.networking;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


class UserSerializer implements JsonSerializer<User> {

  @Override
  public JsonElement serialize(User src, Type typeOfSrc,
      JsonSerializationContext context) {
    JsonObject j = new JsonObject();
    if(src.hasField("userName")) {
      j.addProperty("userName", src.getField("userName"));
    }
    j.addProperty("id", src.userID());
    return j;
  }

}
