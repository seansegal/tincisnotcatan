package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

public class UserGroup implements Group {

  // for group
  private Collection<RequestProcessor> reqs;
  private Set<User>                    users;
  private Map<User, Long>              afk;
  private API                          api;
  private String                       identifier;
  private final int                    desiredSize;
  private String                       groupName;


  private UserGroup() {
    assert false : "should never call this constructor!";
    this.desiredSize = 1;
  }


  private UserGroup(UserGroupBuilder b) {
    // use the fields of the builder to setup
    this.reqs = b.reqs;
    this.desiredSize = b.desiredSize;
    this.identifier = b.identifier;
    this.groupName = b.name;
    // default inits
    this.users = new HashSet<>();
    this.afk = new HashMap<>();
    // initialize api
    if (b.apiClass != null) {
      try {
        this.api = b.apiClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        System.out.println(
            "Error instantiating API class of type:" + b.apiClass.getName());
        e.printStackTrace();
      }
      if (b.apiSettings != null) {
        api.setSettings(b.apiSettings);
      }
    }
  }


  @Override
  public String identifier() {
    return identifier;
  }


  @Override
  public String groupName() {
    return groupName;
  }


  @Override
  public int maxSize() {
    return this.desiredSize;
  }


  @Override
  public int currentSize() {
    return users.size();
  }


  @Override
  public boolean isFull() {
    return this.desiredSize == users.size();
  }


  @Override
  synchronized public boolean add(User u) {

    if (userReconnected(u)) {
      return true;
    }

    if (users.size() == desiredSize) {
      return false; // we're full, don't give me any more users.
    }

    u.setUserID(api.addPlayer(u.getFieldsAsJson()));

    users.add(u);
    for (User other : users) {
      JsonObject gs = api.getGameState(other.userID());
      gs.addProperty(Networking.REQUEST_IDENTIFIER, "getGameState");
      other.message(gs);
    }
    if (isFull()) {
      handleMessage(u, Networking.START_GAME_MESSAGE);
      System.out.println("Game start called: " + identifier);
    }
    return true;
    // regardless of whether or not u was present in the set already,
    // should return true to indicate that u has "found a home"
  }


  @Override
  synchronized public boolean remove(User u) {
    userDisconnected(u,
        System.currentTimeMillis() + Networking.DISCONNECT_TIMEOUT);
    return true;
  }


  @Override
  synchronized public boolean handleMessage(User u, JsonObject j) {
    if (!allUsersConnectedWithMessage()) {
      if (j.has(Networking.REQUEST_IDENTIFIER)
          && !j.get(Networking.REQUEST_IDENTIFIER).getAsString()
              .equals("gameOver")) {
        return false;
      }
    }
    if (!users.contains(u)) {
      System.out.println("Error : user not contained");
      return false;
    }
    for (RequestProcessor req : reqs) {
      if (req.match(j)) {
        return req.run(u, users, j, api);
      }
    }
    return false;
  }


  public boolean afkTick() {
    return allUsersConnectedWithMessage();
  }


  private boolean allUsersConnectedWithMessage() {
    if (afk.isEmpty()) {
      return true;
    }
    JsonObject message =
        Networking.userDisconnectedMessage(Collections.unmodifiableMap(afk));
    users.stream()
        .filter(u -> !afk.containsKey(u))
        .forEach(u -> u.message(message));
    return false;
  }


  public boolean hasUserWithID(String uuid) {
    long count = users.stream()
        .filter(u -> u.hasField(Networking.USER_IDENTIFIER)
            && u.getField(Networking.USER_IDENTIFIER)
                .equals(uuid))
        .count();

    if (count > 0) {
      System.out
          .println("User group " + this.identifier() + " has user " + uuid);
    }
    return count > 0;
  }


  @Override
  public boolean isEmpty() {
    return users.isEmpty();
  }


  private void userDisconnected(User u, long expiresAt) {
    if (!users.contains(u)) {
      System.out.println(
          "Error! Disconnected user, but I don't have a reference to them!");
      return;
    }
    System.out.println("DISCONNECTED AT " + expiresAt + " " + u);
    afk.put(u, expiresAt);
    if (users.size() != afk.size()) {
      allUsersConnectedWithMessage();
    }
  }


  private boolean userReconnected(User u) {
    System.out.println("RECONNECTED " + u);
    if (!afk.containsKey(u)) {
      return false;
    }
    afk.remove(u);
    if (this.allUsersConnectedWithMessage()) {
      System.out.println("SENDING READY TO GO MESSAGE");
      users.stream().forEach(usr -> usr.message(Networking.GAME_READY_MESSAGE));
    }
    return true;
  }


  public static class UserGroupBuilder {

    private Collection<RequestProcessor> reqs;
    private int                          desiredSize = 1;
    private final Class<? extends API>   apiClass;
    private String                       identifier  = null;
    private String                       name        = null;
    private JsonObject                   apiSettings = null;


    public UserGroupBuilder(Class<? extends API> apiClass) {
      // set any required variables
      this.apiClass = apiClass;

      // default to echo processor for now
      Collection<RequestProcessor> r = new ArrayList<>();
      r.add(new EchoProcessor()); // for testing;
      this.reqs = r;
    }


    public UserGroupBuilder withRequestProcessors(
        Collection<RequestProcessor> reqs) {
      this.reqs = reqs;
      return this;
    }


    public UserGroupBuilder withSize(int numUsers) {
      this.desiredSize = numUsers;
      return this;
    }


    public UserGroupBuilder withUniqueIdentifier(String id) {
      this.identifier = id;
      return this;
    }


    public UserGroupBuilder withName(String name) {
      this.name = name;
      return this;
    }


    public UserGroupBuilder withApiSettings(JsonObject j) {
      this.apiSettings = j;
      return this;
    }


    public UserGroup build() {
      return new UserGroup(this);
    }
  }
}
