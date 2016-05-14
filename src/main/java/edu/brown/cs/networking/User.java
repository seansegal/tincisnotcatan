package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.format;

import java.io.IOException;
import java.net.HttpCookie;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * An abstraction for a User in the websocket protocol. By changing sessions
 * dynamically, (on disconnect and reconnect) and identifying sessions from the
 * same end user, the User class provides a persistent Session protocol for
 * websockets.
 *
 * @author ndemarco
 */
public final class User {

  private Session    session;
  @Expose
  private Integer    userID;
  private JsonObject values;


  /**
   * Create a new User with a session.
   *
   * @param s
   *          the session
   */
  public User(Session s) {
    updateSession(s);
  }


  /**
   * Assign a new session to this User.
   *
   * @param s
   *          the new session
   * @return boolean indicating success.
   */
  public boolean updateSession(Session s) {
    if (session != null && session.isOpen()) {
      System.out.println("ERROR! Tried to update an active session.");
      return false;
    }
    this.session = s;
    values = new JsonObject();
    for (HttpCookie cook : s.getUpgradeRequest().getCookies()) {
      values.addProperty(cook.getName(), cook.getValue());
    }
    return true;
  }


  /**
   * @return this User's current session.
   */
  public Session session() {
    return session;
  }


  /**
   * Send a message TO this User's currently active session.
   *
   * @param json
   *          the message to send
   * @return boolean indicating success.
   */
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


  /**
   * Get a customizable field from this User. Fields are defined at construction
   * and session update by the cookies of the session object that this User
   * contains.
   *
   * @param field
   *          the name of the field to query
   * @return the value of that field, if it exists. Null otherwise.
   */
  public String getField(String field) {
    if (values.has(field) && !values.get(field).isJsonNull()) {
      return values.get(field).getAsString();
    }
    return null;
  }


  /**
   * Indicate if this User has a customizable field named {@code user}
   *
   * @param field
   * @return
   */
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
