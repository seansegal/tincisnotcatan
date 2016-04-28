package edu.brown.cs.networking;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@WebSocket
public class ReceivingWebsocket {

  // standing bugs : if a current user in a game goes back to home, and reenters
  // a name, their name doesn't change.
  // on /home, a user that has an existing USER_ID cookie will go to /board, but
  // then redirect to home. probably just js logic.

  private static GCT              gct;
  private final Map<String, User> uuidToUser      = new HashMap<>();
  private final Gson              GSON            = new Gson();
  private final Timer             timer;
  private final Map<User, Long>   afkMap;


  private static final String     USER_IDENTIFIER = "USER_ID";
  private static final String     HEARTBEAT       = "\"HEARTBEAT\"";

  private static final long       ONE_MINUTE      = 60 * 1000;
  private static final long       FIVE_MINUTES    = 5 * ONE_MINUTE;


  public ReceivingWebsocket() {
    afkMap = new ConcurrentHashMap<>();
    timer = new Timer();
    TimerTask cleanup = new TimerTask() {

      @Override
      public void run() {
        synchronized (ReceivingWebsocket.this) {
          long now = System.currentTimeMillis();
          for (User u : afkMap.keySet()) {
            if (now - afkMap.get(u) > FIVE_MINUTES) {
              String id = u.getField(USER_IDENTIFIER);
              uuidToUser.remove(id);
              afkMap.remove(u);
              gct.remove(u);
            }
          }
        }
      }
    };
    timer.schedule(cleanup, 0L, ONE_MINUTE);

  }


  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    List<HttpCookie> list = session.getUpgradeRequest().getCookies();

    if (list.isEmpty()) {
      System.out.println(
          "Error! We got a connection without registration cookies. Should redirect to home.");
      sendError(session, "RESET");
      return;
    }

    if (seenBefore(session)) {
      synchronized (this) {
        System.out.println("I've seen this session before!");
        String id = getSessionID(session);
        assert id != null : "Illegal state : seen session before, but not in map.";
        // assign this session to the existing user:
        User u = uuidToUser.get(id);
        u.updateSession(session);
        u.updateCookies(list);
        afkMap.remove(u);
        System.out.println("Updated User object with new session");
      }
    } else {
      if (getSessionID(session) != null) {
        System.out.println("Saw a session with an expired user id. Resetting.");
        sendError(session, "RESET");
        return;
      }
      System.out.println("New session!");
      String newid = DistinctRandom.getString();
      HttpCookie newCookie = new HttpCookie(USER_IDENTIFIER, newid);
      list.add(newCookie);
      User newUser = gct.register(session, list);
      uuidToUser.put(newid, newUser);
      setCookie(newUser, list);
    }
  }


  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    User u = getUserFor(session);
    if (u != null) {
      System.out.format("User %s was disconnected due to: %s%n", u, reason);
      System.out.format("Marking user %s as AFK %n", u);
      afkMap.put(u, System.currentTimeMillis());
    } else {
      System.out.format("Unregistered session %s was disconnected due to: %s%n",
          session.getLocalAddress(), reason);
    }
  }


  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    if (message.equals(HEARTBEAT)) {
      System.out.format("Heartbeat from %s%n", session.getLocalAddress());
      return;
    }
    User u = getUserFor(session);
    JsonObject j = null;
    try {
      j = GSON.fromJson(message, JsonObject.class);
    } catch (JsonSyntaxException e) {
      System.out.println("Excption while parsing JSON, should return error");
      return;
    }

    if (u != null && j != null) {
      gct.message(u, j);
    }
  }


  private boolean seenBefore(Session s) {
    String id = getSessionID(s);
    if (id != null && uuidToUser.containsKey(id)) {
      return true;
    }
    return false;
  }


  private String getSessionID(Session s) {
    for (HttpCookie c : s.getUpgradeRequest().getCookies()) {
      if (c.getName().equals(USER_IDENTIFIER)) {
        return c.getValue().toString();
      }
    }
    return null;
  }


  private User getUserFor(Session s) {
    String potentialId = getSessionID(s);
    if (potentialId != null) {
      return uuidToUser.get(potentialId);
    }
    return null;
  }


  private void setCookie(User u, List<HttpCookie> cookies) {
    JsonObject j = new JsonObject();
    j.addProperty("requestType", "setCookie");
    j.add("cookies", GSON.toJsonTree(cookies));
    u.message(j);
  }


  private void sendError(Session s, String error) {
    JsonObject j = new JsonObject();
    j.addProperty("requestType", "ERROR");
    j.addProperty("description", error);
    try {
      s.getRemote().sendString(j.toString());
    } catch (IOException e) {
      System.out.println("Error sending error. Doesn't that suck?");
      e.printStackTrace();
    }
  }


  public static void setGCT(GCT terminal) {
    gct = terminal;
  }


}
