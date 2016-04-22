package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

class SessionGroup implements Timestamped {

  private List<Session> sessions;
  private int           size;
  private long          timestamp;
  private String id;


  public SessionGroup(int size, String id) {
    sessions = new ArrayList<>();
    this.size = size;
    this.timestamp = System.currentTimeMillis();
    this.id = id;
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
      System.out.format("Session %s was added to SessionGroup %s%n", s.getLocalAddress(), id);
      return sessions.add(s);
    }
    System.out.format("Session %s was not added to %s, it is full%n", s, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    System.out.format("Session %s was removed from SessionGroup %s%n", s.getLocalAddress(), id);
    boolean toReturn = sessions.remove(s);
    if(isEmpty()){
      System.out.format("SessionGroup %s is now empty.%n", id);
    }
    return toReturn;
  }


  public boolean message(Session s, String message) {
    if (!sessions.contains(s)) {
      System.out.format("Error : attempted to handle message from %s by SessionGroup %s, not contained.%n", s.getLocalAddress(), id);
      return false;
    }
    // handle a message from s! TODO
    System.out.format("Session %s sent a message through SessionGroup %s%n", s.getLocalAddress(), id);
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
