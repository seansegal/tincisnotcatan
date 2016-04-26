package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonObject;


public class ChatHandler implements RequestInterpreter {

  private static final String IDENTIFIER = "chat";


  @Override
  public boolean handle(User<?> user, Collection<User<?>> group,
      JsonObject json) {
    System.out.println("Message processed : " + json.get("message"));

    Collection<String> userIds = new ArrayList<>();
    for(User<?> u : group) {
      userIds.add(String.valueOf(u.userID()));
    }

    JsonObject toSend = Chat.createMessage(String.format("%s%n", user.getField("userName")),
        json.get("message").getAsString(), userIds);

    boolean success = true;
    for(User<?> other : group) {
      success &= other.message(toSend);
    }
    return success;
  }


  @Override
  public String identifier() {
    return IDENTIFIER;
  }


}
