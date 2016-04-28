package edu.brown.cs.networking;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import spark.Spark;

// Grand Central Terminal - Routes all of the inputs to appropriate groups
public class GCT {

  private final PriorityBlockingQueue<Group> pending;
  private final List<Group>                  full;
  private final Map<User, Group>             userToUserGroup;
  private final GroupSelector                groupSelector;
  private static Gson                        GSON;


  private GCT(GCTBuilder builder) {
    // Not provided by builder:
    this.pending = new PriorityBlockingQueue<>();
    this.full = Collections.synchronizedList(new ArrayList<>());
    this.userToUserGroup = new ConcurrentHashMap<>();

    GSON = new GsonBuilder().registerTypeAdapter(Group.class, new GroupSerializer()).create();


    // provided by builder:
    this.groupSelector = builder.groupSelector;
    Spark.webSocket(builder.webSocketRoute, ReceivingWebsocket.class);
    ReceivingWebsocket.setGCT(this);

    if (builder.groupViewRoute != null) {
      Spark.webSocket(builder.groupViewRoute, GroupViewWebsocket.class);
      GroupViewWebsocket.setGCT(this);
    }

    // needed:
    Spark.init();
  }


  public JsonObject openGroups() {
    Collection<Group> list = new CopyOnWriteArrayList<>();
    for (Group g : pending) {
      list.add(new GroupView(g));
    }
    Collection<Group> gr = Collections.unmodifiableCollection(list);
    JsonObject toRet = new JsonObject();
    toRet.add("groups", GSON.toJsonTree(gr));
    return toRet;
  }


  // add verification?? TODO
  public User register(Session s, List<HttpCookie> cookies) {
    User newUser = new User(s, cookies);
    add(newUser);
    return newUser;
  }


  private boolean add(User u) {
    Group bestFit =
        groupSelector.selectFor(u, Collections.unmodifiableCollection(pending));
    assert bestFit != null : "Select for returned a null user group!";

    userToUserGroup.put(u, bestFit);
    bestFit.add(u);

    System.out.format("User %s added to %s%n", u, bestFit);

    if (bestFit.isFull()) {
      full.add(bestFit);
      pending.remove(bestFit);
    } else {
      pending.remove(bestFit); // do nothing if it's new, otherwise reheap.
      pending.add(bestFit);
    }
    GroupViewWebsocket.reportChange(openGroups());
    return true;
  }


  public boolean remove(User u) {
    Group group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    group.remove(u);
    userToUserGroup.remove(u);
    if (group.isEmpty()) {
      full.remove(group);
      pending.remove(group);
    }
    GroupViewWebsocket.reportChange(openGroups());
    return true;
  }


  public boolean message(User u, JsonObject j) {
    Group group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    return group.handleMessage(u, j);
  }


  public static class GCTBuilder {

    private final String  webSocketRoute;
    private String        groupViewRoute;
    private GroupSelector groupSelector = new BasicGroupSelector();


    public GCTBuilder(String route) {
      this.webSocketRoute = route;
    }


    public GCTBuilder withGroupViewRoute(String route) {
      this.groupViewRoute = route;
      return this;
    }


    public GCTBuilder withGroupSelector(GroupSelector selector) {
      this.groupSelector = selector;
      return this;
    }


    public GCT build() {
      return new GCT(this);
    }

  }

}
