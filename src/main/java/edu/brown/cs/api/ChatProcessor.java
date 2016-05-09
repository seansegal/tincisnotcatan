package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;


public class ChatProcessor implements RequestProcessor {

  private static final String    IDENTIFIER  = "chat";
  private static final String    REQUEST_KEY = "requestType";
  private final List<JsonObject> chatLog;


  public ChatProcessor() {
    chatLog = new ArrayList<>();
  }


  @Override
  public boolean run(User user, Collection<User> group, JsonObject json,
      API api) {

    JsonObject toSend =
        Chat.createMessage(String.format("%s%n", user.getField("userName")),
            json.get("message").getAsString());
    toSend.addProperty("fromUser", user.userID());
//    chatLog.add(0, toSend);
//    toSend.add("chatLog", Networking.GSON.toJsonTree(chatLog));

    boolean success = true;
    for (User other : group) {
      success &= other.message(toSend);
    }
    return success;
  }


  @Override
  public boolean match(JsonObject j) {
    if (j.has(REQUEST_KEY) && !j.get(REQUEST_KEY).isJsonNull()) {
      return j.get(REQUEST_KEY).getAsString().equals(IDENTIFIER);
    }
    return false;
  }


}
