package edu.brown.cs.networking;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ActionWebSocketHandler {

  private final GameServer gs = GameServer.getInstance();
  private static final Set<Session> activeUsers = new HashSet<>();

  private static final int NUM_PLAYERS = 4;


  @OnWebSocketConnect
  synchronized public void onConnect(Session user) throws Exception {
    System.out.println("Connected " + user.getLocalAddress());
    if(activeUsers.size() < NUM_PLAYERS) {
      activeUsers.add(user);
    }
  }


  @OnWebSocketClose
  synchronized public void onClose(Session user, int statusCode, String reason) {
    System.out.println("Disconnected from " + user.getLocalAddress());
    if(activeUsers.remove(user)){
      System.out.println("PLAYER CLOSED: " + user.getLocalAddress());
    } else {
      System.out.println("WITNESS CLOSED: " + user.getLocalAddress());
    }
  }


  @OnWebSocketMessage
  synchronized public void onMessage(Session user, String message) {

    if(activeUsers.contains(user)) {
      System.out.println("Received message from " + user.getLocalAddress());
      System.out.println("Message : " + message);
      // do something with that action.
    } else {
      System.out.println("Received invalid message from non-player");
    }
  }

}
