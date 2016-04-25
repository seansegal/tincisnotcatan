package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.api.CatanAPI;

class SessionGroup implements Timestamped {

  private List<Session>         sessions;
  private int                   size;
  private long                  timestamp;
  private String                id;
  private Map<Session, Integer> intForSession;
  private Map<Integer, Session> sessionForInt;

  private final Gson            GSON = new Gson();
  private final CatanAPI        api;


  public SessionGroup(int size, String id) {
    api = new CatanAPI();
    sessions = new ArrayList<>();
    intForSession = new HashMap<>();
    sessionForInt = new HashMap<>();
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
      // TODO: Add player attributes here.
      intForSession.put(s, api.addPlayer(""));
      sessionForInt.put(intForSession.get(s), s);
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
    JsonObject json = GSON.fromJson(message, JsonObject.class);
    System.out.println("Message parsed to : " + json.toString());

    switch (json.get("requestType").getAsString()) {
      case "chat":
        return handleChatMessage(s, json);
      case "action":
        return handleAction(s, json);
      case "getGameState":
        return handleGetGameState(s);
      default:
        System.out.format("Session %s made an illegal request : %s%n",
            s.getLocalAddress(), message);
        return false;
    }
  }


  private boolean handleGetGameState(Session s) {
    JsonObject resp = api.getGameState(intForSession.get(s));
    resp.addProperty("requestType", "getGameState");
    return Broadcast.toSession(s, resp);
  }


  private boolean handleAction(Session s, JsonObject json) {

    json.add("player", GSON.toJsonTree(String.valueOf(intForSession.get(s))));
    System.out.println(json);

    Map<Integer, JsonObject> resp = api.performAction(json.toString());
    for (Integer i : resp.keySet()) {
      Session recipient = sessionForInt.get(i);
      if(recipient == null){
        System.out.format("API thinks there's a player %d, but there isn't an active session.%n", i);
        continue;
      }
      json.add("content", resp.get(i));
      System.out.println(i);
      System.out.println(json.get("requestType").getAsString());
      Broadcast.toSession(recipient, json);
      handleGetGameState(recipient);
    }
    return true;
  }


  private boolean handleChatMessage(Session s, JsonObject json) {

    System.out.println("Message processed : " + json.get("message"));
    return Broadcast.toAll(sessions,
        Chat.createMessage(s.getLocalAddress().toString(),
            json.get("message").getAsString(), userIds()));

  }


  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for (Session s : sessions) {
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

  // message specific user

}
