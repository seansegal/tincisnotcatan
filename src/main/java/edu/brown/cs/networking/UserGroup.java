package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.print;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.gson.JsonObject;

public class UserGroup implements Group {

  private UserTable              table;
  private API                    api;
  private final UserGroupBuilder myBuilder;


  private UserGroup() {
    assert false : "should never call this constructor!";
    this.myBuilder = null;
  }


  private UserGroup(UserGroupBuilder b) {
    this.myBuilder = b;
    this.table = new UserTable();

    if (b.apiClass != null) {
      try {
        this.api = b.apiClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        print("Error instantiating API class of type:" + b.apiClass.getName());
        e.printStackTrace();
      }
      if (b.apiSettings != null) {
        api.setSettings(b.apiSettings);
      }
    }
  }


  @Override
  public boolean add(User u) {
    synchronized (this) {

      if (userReconnected(u)) {
        return true;
      }

      if (isFull()) {
        return false; // we're full, don't give me any more users.
      }

      u.setUserID(api.addPlayer(u.getFieldsAsJson()));
      table.addUser(u);

      for (User other : table.users()) {
        JsonObject gs = api.getGameState(other.userID());
        gs.addProperty(Networking.REQUEST_IDENTIFIER, "getGameState");
        other.message(gs);
      }
      if (isFull()) {
        handleMessage(u, Networking.START_GAME_MESSAGE);
        print("Game start called: " + identifier());
      }
      return true;
      // regardless of whether or not u was present in the set already,
      // should return true to indicate that u has "found a home"

    }
  }


  @Override
  public boolean remove(User u) {
    synchronized (this) {
      userDisconnected(u,
          System.currentTimeMillis() + Networking.DISCONNECT_TIMEOUT);
      return true;
    }
  }


  @Override
  public boolean handleMessage(User u, JsonObject j) {
    synchronized (this) {
      if (!allUsersConnectedWithMessage()) {
        if (j.has(Networking.REQUEST_IDENTIFIER)
            && !j.get(Networking.REQUEST_IDENTIFIER).getAsString()
                .equals("gameOver")) {
          return false;
        }
      }
      if (!table.contains(u)) {
        print("Error : user not contained");
        return false;
      }
      for (RequestProcessor req : myBuilder.reqs) {
        if (req.match(j)) {
          return req.run(u, table.users(), j, api);
        }
      }
      return false;
    }

  }


  private boolean allUsersConnectedWithMessage() {
    if (table.allUsersConnected()) {
      return true;
    }
    JsonObject message =
        Networking.userDisconnectedMessage(
            Collections.unmodifiableMap(table.afkMap()));
    table.onlyConnectedUsers().stream().forEach(u -> u.message(message));
    return false;
  }


  public boolean hasUser(User u) {
    return table.contains(u);
  }


  public boolean hasUser(String id) {
    return table.contains(id);
  }


  @Override
  public boolean isEmpty() {
    return table.isEmpty();
  }


  private void userDisconnected(User u, long expiresAt) {
    if (!table.contains(u)) {
      return; // just ignore it.
    }
    print("DISCONNECTED AT " + expiresAt + " " + u);
    table.userAFK(u, expiresAt);
    new Thread(new CleanupTask(u, table)).start();
    allUsersConnectedWithMessage();
  }


  private boolean userReconnected(User u) {
    if (!table.isAfk(u)) {
      return false;
    }
    print("RECONNECTED " + u);
    table.userNotAFK(u);
    if (this.allUsersConnectedWithMessage()) {
      print("SENDING READY TO GO MESSAGE");
      table.users().stream()
          .forEach(usr -> usr.message(Networking.GAME_READY_MESSAGE));
    }
    return true;
  }


  @Override
  public String identifier() {
    return myBuilder.identifier;
  }


  @Override
  public String groupName() {
    return myBuilder.name;
  }


  @Override
  public int maxSize() {
    return myBuilder.desiredSize;
  }


  @Override
  public int currentSize() {
    return table.size();
  }


  @Override
  public boolean isFull() {
    return myBuilder.desiredSize == table.size();
  }


  public class CleanupTask implements Runnable {

    private User      u;
    private UserTable table;


    public CleanupTask(User u, UserTable t) {
      this.u = u;
      this.table = t;
    }


    @Override
    public void run() {
      while (true) {
        try {
          if (table.expired(u)) {
            System.out.println("User expired!");
            for (User u : table.onlyConnectedUsers()) {
              u.message(Networking.GAME_OVER);
            }
            table.clear();
            return;
          } else {
            Thread.sleep(1000);
          }
        } catch (ExpiredUserException | InterruptedException e) {
          // thread consistency error, can just kill the thread.
          return;
        }

      }
    }
  }


  // MARK: BUILDER --------------------------------------------------

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
