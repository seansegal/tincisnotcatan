package edu.brown.cs.networking;

import java.net.HttpCookie;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class ReceivingHandler {

  private static GCT gct = GCT.getInstance();


  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    List<HttpCookie> list = user.getUpgradeRequest().getCookies();
    gct.register(user, list);
  }


  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    gct.remove(user, statusCode, reason);
  }


  @OnWebSocketMessage
  public void onMessage(Session user, String message) {
    gct.message(user, message);
  }


}
