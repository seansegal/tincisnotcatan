package edu.brown.cs.networking;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;

public final class User<D extends UserData> {

  private Session session;
  private D       data;
  private int     userID;


  public User(Session s, int userID, Class<D> data) {
    this.session = s;
    this.userID = userID;
    try {
      this.data = data.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      System.out.println("Illegal user data type : " + data);
      assert false;
      e.printStackTrace();
    }
  }

  public boolean message(JsonObject json){
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


  public Object getField(String field) {
    return data.getField(field);
  }


  public boolean setField(String field, Object value) {
    return data.setField(field, value);
  }


  public boolean isValid() {
    return session.isOpen() && data.isValid();
  }


  public Session session() {
    return session;
  }


  public int userID() {
    return userID;
  }


  @Override
  public String toString() {
    return String.format("Session : {%s}, UserID : {%s}, data of type %s",
        session.getLocalAddress(), String.valueOf(userID),
        data.getClass().toString());
  }

}
