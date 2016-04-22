package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

class SessionGroup implements Timestamped {

  private List<Session> sessions;
  private int           size;
  private long          timestamp;


  public SessionGroup(int size) {
    sessions = new ArrayList<>();
    this.size = size;
    this.timestamp = System.currentTimeMillis();
  }


  public boolean isFull() {
    return sessions.size() == size;
  }


  public boolean isEmpty() {
    return sessions.isEmpty();
  }


  // currently just add to list, can add notification functionality.
  public boolean add(Session s) {
    if (!isFull()) {
      return sessions.add(s);
    }
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    return sessions.remove(s);
  }


  public boolean message(Session s, String message) {
    if (!sessions.contains(s)) {
      return false;
    }
    // handle a message from s! TODO
    return true;
  }


  public boolean contains(Session s) {
    return sessions.contains(s);
  }


  @Override
  public long initTime() {
    return timestamp;
  }


  @Override
  public void stampNow() {
    this.timestamp = System.currentTimeMillis();
  }

  // broadcast to whole group

  // message specific user

}
