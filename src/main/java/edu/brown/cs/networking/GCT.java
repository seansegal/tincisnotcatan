package edu.brown.cs.networking;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.JsonObject;

import spark.Spark;

// Grand Central Terminal - Routes all of the inputs to appropriate groups
public class GCT {

  private final PriorityBlockingQueue<UserGroup> pending;
  private final List<UserGroup>                  full;
  private final Map<User, UserGroup>             userToUserGroup;

  private Class<? extends API>                   apiClass;
  private final GroupSelector                    groupSelector;

  private static final int                       NEED_TO_GENERALIZE_THIS = 2;
  private static int                             GROUP_ID                = 1;
  private static int                             SESSION_ID              = 1;


  private GCT(GCTBuilder builder) {
    // Not provided by builder:
    this.pending = new PriorityBlockingQueue<>();
    this.full = Collections.synchronizedList(new ArrayList<>());
    this.userToUserGroup = new ConcurrentHashMap<>();

    // provided by builder:
    this.apiClass = builder.apiClass;
    this.groupSelector = builder.groupSelector;
    Spark.webSocket(builder.webSocketRoute, ReceivingHandler.class);
    ReceivingHandler.setGCT(this);

    // needed:
    Spark.init();
  }


  // add verification?? TODO
  public User register(Session s, List<HttpCookie> cookies) {
    User newUser = new User(s, cookies);
    add(newUser);
    return newUser;
  }


  private boolean add(User u) {
    UserGroup bestFit =
        groupSelector.selectFor(u, Collections.unmodifiableCollection(pending));
    assert bestFit != null : "Select for returned a null user group!";
    userToUserGroup.put(u, bestFit);
    bestFit.add(u);
    if (bestFit.isFull()) {
      full.add(bestFit);
      pending.remove(bestFit);
    } else {
      pending.remove(bestFit); // do nothing if it's new, otherwise reheap.
      pending.add(bestFit);
    }
    return true;
  }


  public boolean remove(User u) {
    UserGroup group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    return group.remove(u);
  }


  public boolean message(User u, JsonObject j) {
    UserGroup group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    return group.handleMessage(u, j);
  }


  public static class GCTBuilder {

    private Class<? extends API> apiClass;
    private String               webSocketRoute = "/action";
    private GroupSelector        groupSelector  = new BasicGroupSelector();


    public GCTBuilder(Class<? extends API> apiClass) {
      this.apiClass = apiClass;
    }


    public GCTBuilder withWebsocketRoute(String route) {
      this.webSocketRoute = route;
      return this;
    }


    public GCTBuilder withGroupSorter(GroupSelector selector) {
      this.groupSelector = selector;
      return this;
    }


    public GCT build() {
      return new GCT(this);
    }

  }

}
