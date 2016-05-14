package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.format;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.JsonObject;

@WebSocket
class GroupViewWebsocket {

  private static GCT          gct;
  private static Set<Session> viewers = new HashSet<>();


  @OnWebSocketConnect
  public void onConnect(Session session) throws Exception {
    format("Session %s opened connection to GroupView%n",
        session.getLocalAddress());
    viewers.add(session);
    sendGroupsTo(session, gct.openGroups());
  }


  @OnWebSocketClose
  public void onClose(Session session, int statusCode, String reason) {
    format("Session %s closed connection to GroupView%n",
        session.getLocalAddress());
    viewers.remove(session);
  }


  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    if (message.equals(Networking.HEARTBEAT)) {
      try {
        session.getRemote().sendString(message);
      } catch (IOException e) {
        return;
      }
    }
    return; // ignore all messages;
  }


  private static void sendGroupsTo(Session s, JsonObject groups) {
    try {
      s.getRemote().sendString(groups.toString());
    } catch (IOException e) {
      format(
          "Error sending change in groups to session %s. Doing nothing about it.%n",
          s.getLocalAddress());
    }
  }


  public static void reportChange(JsonObject groups) {
    for (Session s : viewers) {
      sendGroupsTo(s, groups);
    }
  }


  public static void setGCT(GCT gctToSet) {
    gct = gctToSet;
  }

}
