package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class SessionGroup implements Timestamped {

  private List<User<?>>         users;
  private int                   size;
  private long                  timestamp;
  private String                id;
  private Map<User<?>, Integer> userToInt;
  private Map<Integer, User<?>> intToUser;
  private Map<Session, User<?>> sessionToUser;

  private final Gson            GSON = new Gson();
  private final API        api;


  public SessionGroup(int size, String id, API api) {
    this.api = api;
    users = new ArrayList<>();
    userToInt = new HashMap<>();
    intToUser = new HashMap<>();
    sessionToUser = new HashMap<>();
    this.size = size;
    this.timestamp = System.currentTimeMillis();
    this.id = id;
  }


  public boolean isFull() {
    if (users.size() == size){
      boolean usersAreReady = true;
      for(User<?> u: users) {
        usersAreReady &= u.isValid();
      }
      return usersAreReady;
    }
    return false;
  }


  public boolean isEmpty() {
    return users.isEmpty();
  }


  // currently just add to list, can add notification functionality.
  public boolean add(Session s) {
    if (!isFull()) {
      System.out.format("Session %s was added to SessionGroup %s%n",
          s.getLocalAddress(), id);

      // TODO: Add player attributes here. // maybe convert addPlayer to take User(data) instead?
      int apisUserID = api.addPlayer("");
      User<?> u = new User<>(s, apisUserID, api.getUserDataClass());
      userToInt.put(u, apisUserID);
      intToUser.put(apisUserID, u);
      sessionToUser.put(s, u);
      System.out.format("User %s was created and added to a session group %s%n", u, id);
      return users.add(u);
    }
    System.out.format("Session %s was not added to %s, it is full%n", s, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    System.out.format("Session %s was removed from SessionGroup %s%n",
        s.getLocalAddress(), id);
    User<?> u = sessionToUser.get(s);
    userToInt.remove(u);
    intToUser.remove(u.userID());
    boolean toReturn = users.remove(u);

    if (isEmpty()) {
      System.out.format("SessionGroup %s is now empty.%n", id);
    }
    return toReturn;
  }


  public boolean message(Session s, String message) {
    if (!sessionToUser.containsKey(s)) {
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
    User<?> u = sessionToUser.get(s);
    JsonObject resp = api.getGameState(u.userID());
    resp.addProperty("requestType", "getGameState");
    return Broadcast.toSession(s, resp);
  }


  private boolean handleAction(Session s, JsonObject json) {
    User<?> u = sessionToUser.get(s);
    json.add("player", GSON.toJsonTree(String.valueOf(u.userID())));
    System.out.println(json);

    Map<Integer, JsonObject> resp = api.performAction(json.toString());
    for (Integer i : resp.keySet()) {
      Session recipient;
      try {
        recipient = intToUser.get(i).session();
      } catch (NullPointerException e) {
        System.out.format("API thinks there's a player %d, but there isn't an active session.%n", i);
        continue;
      }
      json.add("content", resp.get(i));
      json.add("player", GSON.toJsonTree(i));
      System.out.println(i);
      System.out.println(json.get("requestType").getAsString());
      Broadcast.toSession(recipient, json);
      handleGetGameState(recipient);
    }
    return true;
  }


  private boolean handleChatMessage(Session s, JsonObject json) {

    System.out.println("Message processed : " + json.get("message"));
    return Broadcast.toUsers(users,
        Chat.createMessage(s.getLocalAddress().toString(),
            json.get("message").getAsString(), userIds()));

  }


  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for (User<?> u : users) {
      toRet.add(u.session().getLocalAddress().toString());
    }
    return toRet;
  }


  public boolean contains(Session s) {
    for(User<?> u : users){
      if(u.session().equals(s)){
        return true;
      }
    }
    return false;
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
