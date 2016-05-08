package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.print;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.JsonObject;

@WebSocket
public class NewWebsocket {

  private final ExecutorService   threadPool;
  private final Map<String, User> uuidToUser;
  private final Set<Session>      ignoreSession;
  private static GCT              gct;


  public NewWebsocket() {
    threadPool = Executors.newCachedThreadPool();
    uuidToUser = new ConcurrentHashMap<>();
    ignoreSession = new ConcurrentHashSet<>();
  }


  // Connecting and session management


  @OnWebSocketConnect
  public void onConnect(Session s) {
    if (sessionIsExpired(s)) {
      print("Expired Session");
      sendError(s, "RESET");
      return;
    }
    User u = userForSession(s);
    if (u != null) { // existing user with old session, update it.
      if (!u.updateSession(s)) {
        // tried to update an open session - TWO TAB MOFO
        ignoreSession.add(s);
        this.sendError(s, "DUPLICATE_TAB");
        return;
      }
    } else {
      u = createNewUser(s);
    }
    System.out.println("Submitting connect");
    Future<?> f = threadPool.submit(new ConnectUserTask(u, gct));
    try {
      f.get(); // blocks!
      System.out.println("Connect complete");
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

  }


  private boolean sessionIsExpired(Session s) {
    return s.getUpgradeRequest().getCookies().stream()
        .anyMatch(c -> c.getName().equals(Networking.USER_IDENTIFIER)
            && !uuidToUser.containsKey(c.getValue()));
  }


  private User createNewUser(Session s) {
    User u = new User(s);
    List<HttpCookie> cookies = s.getUpgradeRequest().getCookies();
    String id = DistinctRandom.getString();
    uuidToUser.put(id, u);
    cookies.add(new HttpCookie(Networking.USER_IDENTIFIER, id));
    setCookie(u, cookies);
    return u;
  }


  private void sendError(Session s, String error) {
    JsonObject j = new JsonObject();
    j.addProperty(Networking.REQUEST_IDENTIFIER, "ERROR");
    j.addProperty("description", error);
    try {
      s.getRemote().sendString(j.toString());
    } catch (IOException e) {
      System.out.println("Error sending error. Doesn't that suck?");
      e.printStackTrace();
    }
  }


  private void setCookie(User u, List<HttpCookie> cookies) {
    JsonObject j = new JsonObject();
    j.addProperty(Networking.REQUEST_IDENTIFIER, "setCookie");
    j.add("cookies", Networking.GSON.toJsonTree(cookies));
    u.message(j);
  }


  @OnWebSocketClose
  public void onClose(Session s, int statusCode, String reason) {
    if (ignoreSession.contains(s)) {
      ignoreSession.remove(s);
      return;
    }
    User u = userForSession(s);
    if (u == null) {
      System.out
          .println("Disconnected user we've never seen before. Do nothing");
      return; // do nothing with a disconnected user we've never seen.
    }
    System.out.println("Submitting disconnect");
    Future<?> f =
        threadPool.submit(new DisconnectUserTask(u, statusCode, reason, gct));
    try {
      f.get();
      System.out.println("Disconnect complete");
    } catch (InterruptedException | ExecutionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  @OnWebSocketMessage
  public void onMessage(Session s, String msg) {
    if (ignoreSession.contains(s)) {
      return; // ignore messages from duplicate sessions
    }
    System.out.println(msg);
    User u = userForSession(s);
    if (u == null) {
      System.out
          .println("Message from user we've never seen before. Ignoring.");
      return; // do nothing with an unfamiliar session
    }
    threadPool.submit(new MessageUserTask(u, msg, gct));
  }


  private User userForSession(Session s) {
    List<HttpCookie> list = s.getUpgradeRequest().getCookies().stream()
        .filter(c -> c.getName()
            .equals(Networking.USER_IDENTIFIER))
        .collect(Collectors.toList());

    if (list.size() > 1) {
      System.out.println("Error! Improperly formatted cookies. " + list.size());
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
