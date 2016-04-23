package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

class SessionGroup implements Timestamped {

  private List<Session> sessions;
  private int           size;
  private long          timestamp;
  private String        id;

  private final Gson    GSON = new Gson();


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
      System.out.format("Session %s was added to SessionGroup %s%n",
          s.getLocalAddress(), id);
      return sessions.add(s);
    }
    System.out.format("Session %s was not added to %s, it is full%n", s, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    System.out.format("Session %s was removed from SessionGroup %s%n",
        s.getLocalAddress(), id);
    boolean toReturn = sessions.remove(s);
    if (isEmpty()) {
      System.out.format("SessionGroup %s is now empty.%n", id);
    }
    return toReturn;
  }


  public boolean message(Session s, String message) {
    if (!sessions.contains(s)) {
      System.out.format(
          "Error : attempted to handle message from %s by SessionGroup %s, not contained.%n",
          s.getLocalAddress(), id);
      return false;
    }
    System.out.format(
        "Session %s sent a message : %s through SessionGroup %s%n",
        s.getLocalAddress(), message, id);

    @SuppressWarnings("unchecked")
    Map<String, Object> rawMap = GSON.fromJson(message, Map.class);

    String typeOfRequest = SetExtractor.getSingleElement(rawMap.keySet());
    switch (typeOfRequest) {
      case "chat":
        return handleChatMessage(s, rawMap.get("chat"));
      case "action":
        System.out.format("Action requested : %s", rawMap.get("action"));
        return true;
      default:
        System.out.format("Session %s made an illegal request : %s%n",
            s.getLocalAddress(), message);
        return false;
    }

  }


  public boolean handleChatMessage(Session s, Object message) {

    String cast;
    try {
      cast = (String) message;
    } catch (ClassCastException e) {
      System.out.format("ClassCastException: Session %s sent %s through /chat%n",
          s.getLocalAddress(), message);
      return false;
    }

    System.out.println("Message processed : " + cast);
    Broadcast.message(sessions,
        Chat.createMessage(s.getLocalAddress().toString(), cast, userIds()).toString());
    return true;
  }

  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for(Session s : sessions) {
      toRet.add(s.getLocalAddress().toString());
    }
    return toRet;
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
