package edu.brown.cs.networking;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class NewWebsocket {

  private final ExecutorService   threadPool;
  private final Map<String, User> uuidToUser;
  private static GCT              gct;

  private static final String     USER_IDENTIFIER = "USER_ID";


  public NewWebsocket() {
    threadPool = Executors.newCachedThreadPool();
    uuidToUser = new ConcurrentHashMap<>();
  }


  @OnWebSocketConnect
  public void onConnect(Session s) {
    User u = userForSession(s);
    if (u == null) {
      u = gct.register(s);
    } else {
      u.updateSession(s); // existing user with old session, update it.
    }
    threadPool.submit(new ConnectUserTask(u, gct));
  }


  @OnWebSocketClose
  public void onClose(Session s, int statusCode, String reason) {
    User u = userForSession(s);
    if(u == null) {
      System.out.println("Disconnected user we've never seen before. Do nothing");
      return; // do nothing with a disconnected user we've never seen.
    }
    threadPool.submit(new DisconnectUserTask(u, statusCode, reason, gct));
  }


  @OnWebSocketMessage
  public void onMessage(Session s, String msg) {
    User u = userForSession(s);
    if(u == null) {
      System.out.println("Error: Message from user we've never seen before!");
      return; // do nothing with an unfamiliar session
    }
    threadPool.submit(new MessageUserTask(u, msg, gct));
  }

  private User userForSession(Session s) {
    List<HttpCookie> list = s.getUpgradeRequest().getCookies().stream()
        .filter(c -> c.getName()
            .equals(USER_IDENTIFIER))
        .collect(Collectors.toList());

    if (list.size() > 1) {
      System.out.println("Error! Improperly formatted cookies.");
    }

    if (list.isEmpty()) {
      return null;
    } else {
      return uuidToUser.get(list.get(0).getValue());
    }
  }


  public static void setGct(GCT gctToSet) {
    gct = gctToSet;
  }


}
