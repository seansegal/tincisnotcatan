package edu.brown.cs.networking;

import com.google.gson.JsonObject;

public interface RequestProcessor {

  boolean run(User user, Group g, JsonObject json, API api);


  boolean match(JsonObject json);


}
