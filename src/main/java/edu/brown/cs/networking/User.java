package edu.brown.cs.networking;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

public final class User {

  private Session          session;
  private List<HttpCookie> cookies;
  @Expose
  private Integer          userID;
  private JsonObject       values;


  public User(Session s, List<HttpCookie> cookies) {
    this.session = s;
    this.cookies = cookies;
    this.values = new JsonObject();
    for (HttpCookie cook : cookies) {
      values.addProperty(cook.getName(), cook.getValue());
    }
  }


  public void updateSession(Session s) {
    this.session = s;
  }


  public void updateCookies(List<HttpCookie> cookies) {
    this.cookies = cookies;
  }


  public boolean message(JsonObject json) {
    if (session().isOpen()) {
      try {
        session().getRemote().sendString(json.toString());
        return true;
      } catch (IOException e) {
        System.out.format("Failed to send message to Session %s : %s%n",
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
    return values.has(field);
  }


  public JsonObject getFieldsAsJson() {
    return values;
  }


  public Session session() {
    return session;
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
        cookies.toString());
  }

}
