package edu.brown.cs.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;

public class Broadcast {

  public static boolean toAll(Collection<Session> col, JsonObject message) {
    boolean success = true;
    for(Session sesh : col) {
      success &= toSession(sesh, message);
    }
    return success;
  }

  public static boolean toSession(Session s, JsonObject message) {
    if(s.isOpen()) {
      try {
        s.getRemote().sendString(message.toString());
        return true;
      } catch (IOException e) {
        System.out.format("Failed to send message to Session %s : %s%n", s.getLocalAddress(), message);
      }
    }
    return false;
  }

  public static boolean toUsers(Collection<User<?>> col, JsonObject message) {
    Collection<Session> toPass = new ArrayList<>();
    for(User<?> u : col) {
      toPass.add(u.session());
    }
    return toAll(toPass, message);

  }
}
