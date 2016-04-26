package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class SessionGroup implements Timestamped {

  private static final String REQUEST_IDENTIFIER = "requestType";

  private List<User<?>>       users;
  private int                 size;
  private long                timestamp;
  private String              id;

  private final Gson          GSON               = new Gson();
  private final API           api;


  public SessionGroup(int size, String id, API api) {
    this.api = api;
    this.size = size;
    this.timestamp = System.currentTimeMillis();
    this.id = id;
    this.users = new ArrayList<>();
  }


  public boolean isFull() {
    return users.size() == size;
  }


  public boolean isEmpty() {
    return users.isEmpty();
  }


  // currently just add to list, can add notification functionality.
  public boolean add(Session s) {
    if (!isFull()) {
      System.out.format("Session %s was added to SessionGroup %s%n",
          s.getLocalAddress(), id);

      // TODO: Add player attributes here. // maybe convert addPlayer to take
      // User(data) instead?
      int apisUserID = api.addPlayer("");
      User<?> u = new User<>(s, apisUserID, api.getUserDataClass());
      System.out.format("User %s was created and added to session group %s%n",
          u, id);
      return users.add(u);
    }
    System.out.format("Session %s was not added to %s, it is full%n", s, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    System.out.format("Session %s was removed from SessionGroup %s%n",
        s.getLocalAddress(), id);
    User<?> u = getUser(s);
    boolean toReturn = users.remove(u);
    if (isEmpty()) {
      System.out.format("SessionGroup %s is now empty.%n", id);
    }
    // TODO : notify other players that user left,
    // begin countdown to game cancel.
    return toReturn;
  }


  public boolean message(Session s, String message) {
    if (!users.contains(getUser(s))) {
      System.out.format(
          "Error : attempted to handle message from %s by SessionGroup %s, not contained.%n",
          s.getLocalAddress(), id);
      return false;
    }

    User<?> u = getUser(s);

    System.out.format(
        "Session %s sent a message : %s through SessionGroup %s%n",
        s.getLocalAddress(), message, id);

    JsonObject json = GSON.fromJson(message, JsonObject.class);
    if (json.has(REQUEST_IDENTIFIER)
        && !json.get(REQUEST_IDENTIFIER).isJsonNull()) {
      switch (json.get(REQUEST_IDENTIFIER).getAsString()) {
        case "registerUser":
          return registerUser(u, json);
        case "chat":
          return handleChatMessage(u, json);
        case "action":
          return handleAction(u, json);
        case "getGameState":
          return handleGetGameState(u);
        default:
          System.out.format("User %s made an illegal request : %s%n",
              u, message);
          json.addProperty("ERROR",
              "Illegal request: " + json.get(REQUEST_IDENTIFIER).getAsString());
      }

    } else {
      json.addProperty("ERROR", "No " + REQUEST_IDENTIFIER + " field specified");
    }
    return u.message(json);
  }


  private boolean handleGetGameState(User<?> u) {
    return new GetGameStateProcessor(api).run(u, users, new JsonObject());
  }


  private boolean handleAction(User<?> u, JsonObject json) {
    return new ActionProcessor(api).run(u, users, json);
  }


  private boolean handleChatMessage(User<?> u, JsonObject json) {
    return new ChatProcessor().run(u, users, json);
  }


  private boolean registerUser(User<?> u, JsonObject json) {
    System.out.println("Register user called: got: " + json.toString());
    // TODO: reflection to always add the right fields of the class object
    // given?
    // hardcoded for now.
    if (json.has("userName") && json.has("numPlayersDesired")) {
      if (!json.get("userName").isJsonNull()
          && !json.get("numPlayersDesired").isJsonNull()) {
        String username = json.get("userName").getAsString();
        Integer numPlayersDesired = json.get("numPlayersDesired").getAsInt();
        u.setField("userName", username);
        u.setField("gameSize", numPlayersDesired);
        u.setField("color", "PLACEHOLDER");
      } else {
        json.addProperty("ERROR",
            "registration field userName or numPlayersDesired was null");
      }
    } else {
      System.out.println("Bad register user");
      // TODO: ERROR HANDLING
      json.addProperty("ERROR", "Bad register user.");
    }
    return u.message(json); // indicate success by omitting ERROR
                                      // field;
  }


  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for (User<?> u : users) {
      toRet.add(String.format("%s", u.getField("userName")));
    }
    return toRet;
  }


  public boolean contains(Session s) {
    for (User<?> u : users) {
      if (u.session().equals(s)) {
        return true;
      }
    }
    return false;
  }


  private User<?> getUser(int id) {
    return getUserBy(p -> p.userID() == id);
  }


  private User<?> getUser(Session s) {
    return getUserBy(p -> p.session().equals(s));
  }


  private User<?> getUserBy(Predicate<User<?>> p) {
    List<User<?>> list = users.stream().filter(p).collect(Collectors.toList());
    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    assert false : "Non-unique identifying predicate!";
    return null;
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
