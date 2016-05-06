package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class UserGroup implements Timestamped, Group {


  // for timestamped
  private long                         initTime;
  // for group
  private Collection<RequestProcessor> reqs;
  private Set<User>                    users;
  private Map<User, Long>              afk;
  private API                          api;
  private String                       identifier;
  private final int                    desiredSize;
  private String                       groupName;
  private static final Gson            GSON = new GsonBuilder()
      .registerTypeAdapter(User.class, new UserSerializer()).create();


  private UserGroup() {
    assert false : "should never call this constructor!";
    this.desiredSize = 1;
  }


  private UserGroup(UserGroupBuilder b) {
    initTime = System.currentTimeMillis();
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
    if (users.size() == desiredSize) {
      return false; // we're full, don't give me any more users.
    }

    u.setUserID(api.addPlayer(u.getFieldsAsJson()));

    users.add(u);
    for (User other : users) {
      JsonObject gs = api.getGameState(other.userID());
      gs.addProperty("requestType", "getGameState");
      other.message(gs);
    }
    if (isFull()) {
      JsonObject gameStart = new JsonObject();
      gameStart.addProperty("requestType", "action");
      gameStart.addProperty("action", "startGame");
      handleMessage(u, gameStart);
      System.out.println("Game start called: " + identifier);
    }
    return true;
    // regardless of whether or not u was present in the set already,
    // should return true to indicate that u has "found a home"
  }


  @Override
  synchronized public boolean remove(User u) {
    return users.remove(u); // could it be this simple? we can add more logic
                            // for timeouts etc later.
  }


  @Override
  synchronized public boolean handleMessage(User u, JsonObject j) {
    if (!allUsersConnectedWithMessage()) {
      if(j.has("requestType") && !j.get("requestType").getAsString().equals("gameOver")){
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
    JsonObject message = new JsonObject();
    message.addProperty("requestType", "disconnectedUsers");
    Set<User> disconUsers = new HashSet<>();
    long smallestExpire = Long.MAX_VALUE;
    for (User u : afk.keySet()) {
      if (afk.get(u) < smallestExpire) {
        smallestExpire = afk.get(u);
      }
      disconUsers.add(u);
    }
    message.add("users", GSON.toJsonTree(disconUsers));
    message.addProperty("expiresAt", smallestExpire + (1000 * 10));
    users.stream()
        .filter(u -> !afk.containsKey(u))
        .forEach(u -> u.message(message));
    return false;
  }


  public boolean hasUserWithID(String uuid) {
    long count = users.stream()
        .filter(u -> u.hasField("USER_ID") && u.getField("USER_ID")
            .equals(uuid))
        .count();
    if(count > 0) {
      System.out.println("User group " + this.identifier() + " has user " + uuid);
    }
    return count > 0;
  }


  @Override
  public long initTime() {
    return initTime;
  }


  @Override
  public void stampNow() {
    this.initTime = System.currentTimeMillis();

  }


  @Override
  public boolean isEmpty() {
    return users.isEmpty();
  }


  @Override
  public void userDisconnected(User u, long expiresAt) {
    if (!users.contains(u)) {
      System.out.println(
          "Error! Disconnected user, but I don't have a reference to them!");
      return;
    }
    System.out.println("DISCONNECTED AT " + expiresAt + " " + u);
    afk.put(u, expiresAt);

  }


  @Override
  public void userReconnected(User u) {
    System.out.println("RECONNECTED " + u);
    afk.remove(u);
    if (this.allUsersConnectedWithMessage()) {
      System.out.println("SENDING READY TO GO MESSAGE");
      JsonObject readyToGo = new JsonObject();
      readyToGo.addProperty("requestType", "disconnectedUsers");
      readyToGo.add("users", GSON.toJsonTree(Collections.emptyList()));
      readyToGo.addProperty("expiresAt", -1);
      users.stream().forEach(usr -> usr.message(readyToGo));
    }
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
