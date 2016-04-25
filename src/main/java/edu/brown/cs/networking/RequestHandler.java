package edu.brown.cs.networking;

import com.google.gson.JsonObject;

public interface RequestHandler {

  JsonObject handle(User<?> user, JsonObject json);

  boolean requestFits(JsonObject json);

}
