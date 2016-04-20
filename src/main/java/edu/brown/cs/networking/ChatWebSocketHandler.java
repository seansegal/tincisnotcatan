package edu.brown.cs.networking;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

@WebSocket
public class ChatWebSocketHandler {

  private String    sender, msg;
  private final int NUM_USERS = 2;
  private final GameServer gs = GameServer.getInstance();



  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    System.out.println("Connected " + user);
    if (gs.activeUsers() < NUM_USERS) {
      String username = "User" + gs.getAndIncrementId();
      gs.add(user, username);
      gs.broadcastMessage(sender = "Server",
          msg = (username + " joined the chat"));
    } else {
      user.getRemote().sendString(
          String.valueOf(new JSONObject().put("ERROR", "This chat is full.")));
    }
  }


  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    if (gs.contains(user)) {
      String username = gs.get(user);
      gs.remove(user);
      gs.broadcastMessage(sender = "Server",
          msg = (username + " left the chat"));
    }
  }


  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    if (gs.contains(user)) {
      gs.broadcastMessage(sender = gs.get(user),
          msg = message);
    }
  }

}

