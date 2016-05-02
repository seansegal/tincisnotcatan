package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;


public class ChatProcessor implements RequestProcessor {

  private static final String IDENTIFIER = "chat";
  private static final String REQUEST_KEY = "requestType";


  @Override
  public boolean run(User user, Collection<User> group,
      JsonObject json, API api) {
    System.out.println("Message processed : " + json.get("message"));

    Collection<String> userIds = new ArrayList<>();
    for (User u : group) {
      userIds.add(String.valueOf(u.userID()));
    }

    JsonObject toSend =
        Chat.createMessage(String.format("%s%n", user.getField("userName")),
            json.get("message").getAsString(), userIds);
    toSend.addProperty("fromUser", user.userID());

    boolean success = true;
    for (User other : group) {
      success &= other.message(toSend);
    }
    return success;
  }


  @Override
  public boolean match(JsonObject j) {
    if(j.has(REQUEST_KEY) && !j.get(REQUEST_KEY).isJsonNull()){
      return j.get(REQUEST_KEY).getAsString().equals(IDENTIFIER);
    }
    return false;
  }


}
