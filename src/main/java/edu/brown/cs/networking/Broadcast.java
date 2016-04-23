package edu.brown.cs.networking;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jetty.websocket.api.Session;

public class Broadcast {

  public static void message(Collection<Session> col, String message) {
    for(Session sesh : col) {
      if(sesh.isOpen()) {
        try {
          sesh.getRemote().sendString(message);
        } catch (IOException e) {
          System.out.format("Failed to send message to Session %s : %s%n", sesh.getLocalAddress(), message);
        }
      }
    }
  }
}
