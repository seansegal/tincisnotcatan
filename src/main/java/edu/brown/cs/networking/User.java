package edu.brown.cs.networking;
import static edu.brown.cs.networking.Util.format;

import java.io.IOException;
import java.net.HttpCookie;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public final class User {

  private Session    session;
  @Expose
  private Integer    userID;
  private JsonObject values;


  public User(Session s) {
    updateSession(s);
  }


  public void updateSession(Session s) {
    this.session = s;
    values = new JsonObject();
    for (HttpCookie cook : s.getUpgradeRequest().getCookies()) {
      values.addProperty(cook.getName(), cook.getValue());
    }
  }


  public Session session() {
    return session;
  }


  public boolean message(JsonObject json) {
    if (session.isOpen()) {
      try {
        session.getRemote().sendString(json.toString());
        return true;
      } catch (IOException e) {
        format("Failed to send message to Session %s : %s%n",
            userID(), json.toString());
      }
    }
    return false;
  }


  public String getField(String field) {
    if (values.has(field) && !values.get(field).isJsonNull()) {
      return values.get(field).getAsString();
    }
    return null;
  }


  public boolean hasField(String field) {
    return values.has(field) && !values.get(field).isJsonNull();
  }


  public JsonObject getFieldsAsJson() {
    return values;
  }


  public int userID() {
    return userID;
  }


  public void setUserID(int id) {
    assert userID == null : "Tried to set userID more than once!";
    this.userID = id;
  }


  @Override
  public String toString() {
    return String.format("Session : {%s}, UserID : {%s}, data {%s}",
        session.getLocalAddress(), String.valueOf(userID),
        values.toString());
  }

}
