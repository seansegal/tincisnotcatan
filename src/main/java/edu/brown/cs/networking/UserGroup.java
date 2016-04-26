package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

class UserGroup implements Timestamped {

  private static final String REQUEST_IDENTIFIER = "requestType";

  private List<User>          users;
  private int                 size;
  private long                timestamp;
  private String              id;

  private final Gson          GSON               = new Gson();
  private final API           api;


  public UserGroup(int size, String id, API api) {
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
  public boolean add(User u) {
    if (!isFull()) {
      System.out.format("User %s was added to UserGroup %s%n", u, id);

      u.setUserID(api.addPlayer(u.toString())); // TEMPORARY WONT WORK TODO
      System.out.format("User %s was created and added to UserGroup %s%n", u,
          id);
      return users.add(u);
    }
    System.out.format("User %s was not added to %s, it is full%n", u, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(User u) {
    System.out.format("Session %s was removed from SessionGroup %s%n", u, id);
    boolean toReturn = users.remove(u);
    if (isEmpty()) {
      System.out.format("UserGroup %s is now empty.%n", id);
    }
    // TODO : notify other players that user left,
    // begin count down to game cancel.
    return toReturn;
  }


  public boolean message(User u, String message) {
    if (!users.contains(u)) {
      System.out.format(
          "Error : attempted message from %s by UserGroup %s, not here.%n", u,
          id);
      return false;
    }

    System.out.format(
        "User %s sent a message : %s through UserGroup %s%n", u, message, id);

    JsonObject json = GSON.fromJson(message, JsonObject.class);
    // TODO : simplify this into data-oriented model:
    if (json.has(REQUEST_IDENTIFIER)
        && !json.get(REQUEST_IDENTIFIER).isJsonNull()) {
      switch (json.get(REQUEST_IDENTIFIER).getAsString()) {
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
      json.addProperty("ERROR",
          "No " + REQUEST_IDENTIFIER + " field specified");
    }
    return u.message(json);
  }


  private boolean handleGetGameState(User u) {
    return new GetGameStateProcessor(api).run(u, users, new JsonObject());
  }


  private boolean handleAction(User u, JsonObject json) {
    return new ActionProcessor(api).run(u, users, json);
  }


  private boolean handleChatMessage(User u, JsonObject json) {
    return new ChatProcessor().run(u, users, json);
  }


  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for (User u : users) {
      toRet.add(String.format("%s", u.getField("userName")));
    }
    return toRet;
  }


  public boolean contains(Session s) {
    for (User u : users) {
      if (u.session().equals(s)) {
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
