package edu.brown.cs.networking;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jetty.websocket.api.Session;

public class Broadcast {

  public static void toAll(Collection<Session> col, String message) {
    for(Session sesh : col) {
      toSession(sesh, message);
    }
  }

  public static void toSession(Session s, String message) {
    if(s.isOpen()) {
      try {
        s.getRemote().sendString(message);
      } catch (IOException e) {
        System.out.format("Failed to send message to Session %s : %s%n", s.getLocalAddress(), message);
      }
    }
  }
}
