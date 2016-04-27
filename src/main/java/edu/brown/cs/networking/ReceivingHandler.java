package edu.brown.cs.networking;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

@WebSocket
public class ReceivingHandler {

  private static GCT                     gct;
  private static final Map<String, User> uuidToUser      = new HashMap<>();
  private static final Gson              GSON            = new Gson();

  private static final String            USER_IDENTIFIER = "CATAN_USER_ID";


  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    List<HttpCookie> list = session.getUpgradeRequest().getCookies();
    if (list.isEmpty()) {
      System.out.println(
          "Error! We got a connection without registration cookies. Should redirect to home.");
      return;
    }
    // NEED TO CHECK IF COOKIE EXISTS, BUT WE DON'T HAVE INFO -
    // means that the server went down, but the cookie was saved. TODO
    if (seenBefore(session)) {
      System.out.println("I've seen this session before!");
    } else {
      if (getSessionIDCookie(session) != null) {
        System.out.println("Saw a session with an expired user id. Resetting.");
        sendError(session, "RESET");
        return;
      }
      System.out.println("New session!");
      String newid = UUID.randomUUID().toString();
      HttpCookie newCookie = new HttpCookie(USER_IDENTIFIER, newid);
      list.add(newCookie);
      User newUser = gct.register(session, list);
      uuidToUser.put(newid, newUser);
      setCookie(newUser, list);
    }
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

  private void sendError(User u, String error) {
    JsonObject j = new JsonObject();
    j.addProperty("ERROR", error);
    u.message(j);
  }

  private boolean seenBefore(Session s) {
    HttpCookie id = getSessionIDCookie(s);
    if (id != null && uuidToUser.containsKey(id.getValue())) {
      return true;
    }
    return false;
  }


  private HttpCookie getSessionIDCookie(Session s) {
    for (HttpCookie c : s.getUpgradeRequest().getCookies()) {
      if (c.getName().equals(USER_IDENTIFIER)) {
        return c;
      }
    }
    return null;
  }


  private void setCookie(User u, List<HttpCookie> cookies) {
    JsonObject j = new JsonObject();
    j.addProperty("requestType", "setCookie");
    j.add("cookies", GSON.toJsonTree(cookies));
    u.message(j);
  }


  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    if (seenBefore(session)) {
      HttpCookie id = getSessionIDCookie(session);
      if (id != null && uuidToUser.containsKey(id.toString())) {
        User u = uuidToUser.get(id.toString());
        gct.remove(u);
      }
    }
  }


  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    User u = null;
    if (seenBefore(session)) {
      String id = getSessionIDCookie(session).getValue().toString();
      if (id != null && uuidToUser.containsKey(id)) {
        u = uuidToUser.get(id);
      }
    }
    JsonObject j = null;
    try {
      j = GSON.fromJson(message, JsonObject.class);
    } catch (JsonSyntaxException e) {
      System.out.println("Excption while parsing JSON, should return error");
      return;
    }

    if (u != null && j != null) {
      System.out.println("message successful");
      gct.message(u, j);
    }
  }


  public static void setGCT(GCT terminal) {
    gct = terminal;
  }


}
