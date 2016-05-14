package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.util.ConcurrentHashSet;

import com.google.gson.JsonObject;

import spark.Spark;

/**
 * Grand Central Terminal - Routes all of the inputs to appropriate groups
 *
 * @author ndemarco
 */
public final class GCT {

  private final Set<Group>       pending;
  private final Set<Group>       full;
  private final Map<User, Group> userToUserGroup;
  private final GroupSelector    groupSelector;

  private static final int       GAME_LIMIT = 20;


  private GCT(GCTBuilder builder) {
    // Not provided by builder:
    this.pending = new ConcurrentHashSet<>();
    this.full = new ConcurrentHashSet<>();
    this.userToUserGroup = new ConcurrentHashMap<>();

    // provided by builder:
    this.groupSelector = builder.groupSelector;
    Spark.webSocket(builder.webSocketRoute, ReceivingWebsocket.class);
    ReceivingWebsocket.setGct(this);

    // build group view websocket, if user wants it.
    if (builder.groupViewRoute != null) {
      Spark.webSocket(builder.groupViewRoute, GroupViewWebsocket.class);
      GroupViewWebsocket.setGCT(this);
    }
    Spark.init();
  }


  /**
   * @return the maximum number of concurrent groups that this GCT supports.
   */
  public int groupLimit() {
    return GAME_LIMIT;
  }


  /**
   * Get the Group that {@code u} is currently in, else {@code null} if no such
   * group exists.
   *
   * @param u
   *          the user to query
   * @return the {@code Group} that {@code u} is in.
   */
  public Group groupForUser(User u) {
    return userToUserGroup.get(u);
  }


  /**
   * Indicate if the given {@code uuid} represents a user that is presently in
   * any game held by this GCT.
   *
   * @param uuid
   * @return true if this user is presently in any group in this GCT.
   */
  public boolean userIDIsValid(String uuid) {
    return pending.stream().anyMatch(grp -> grp.hasUser(uuid))
        || full.stream().anyMatch(grp -> grp.hasUser(uuid));
  }


  /**
   * @return a JsonObject representing the groups that are currently not full,
   *         and in need of more users.
   */
  public JsonObject openGroups() {
    refreshGroups();
    Collection<Group> list = new ArrayList<>();
    pending.stream().filter(g -> !g.isEmpty())
        .forEach(g -> list.add(new GroupView(g)));
    Collection<Group> gr = Collections.unmodifiableCollection(list);
    JsonObject toRet = new JsonObject();
    toRet.add("groups", Networking.GSON.toJsonTree(gr));
    boolean atLim = pending.size() + full.size() >= GAME_LIMIT;
    toRet.addProperty("atLimit", atLim);
    return toRet;
  }


  /**
   * @return a JsonObject representing the groups that are currently full, and
   *         not in need of more users.
   */
  public JsonObject closedGroups() {
    refreshGroups();
    Collection<Group> list = new ArrayList<>();
    full.stream().forEach(g -> list.add(new GroupView(g)));
    Collection<Group> gr = Collections.unmodifiableCollection(list);
    JsonObject toRet = new JsonObject();
    toRet.add("closedGroups", Networking.GSON.toJsonTree(gr));
    return toRet;

  }


  private void refreshGroups() {
    for (Group g : full) {
      if (g.isEmpty()) {
        full.remove(g);
      } else if (!g.isFull()) {
        full.remove(g);
        pending.add(g);
      }
    }
    for (Group g : pending) {
      if (g.isEmpty()) {
        pending.remove(g);
      } else if (g.isFull()) {
        pending.remove(g);
        full.add(g);
      }
    }
  }


  /**
   * Attempt to add a user, either to a currently non-full group, or to a new
   * group if no such group is found by the GroupSelector. If {@code u} is
   * already in a group, the add message is forwarded to that group, and the
   * group can handle it accordingly. For example, if a {@code User} was
   * previously marked as absent, the group can note that the user has returned.
   *
   * @param u
   *          the {@code User} to add.
   * @return true if the addition succeeded.
   */
  public boolean add(User u) {
    Group bestFit =
        groupSelector.selectFor(u, Collections.unmodifiableCollection(pending));
    if (bestFit == null) {
      return false;
    }

    userToUserGroup.put(u, bestFit);
    bestFit.add(u);

    format("User %s added to %s%n", u, bestFit);
    filterGroup(bestFit);
    return true;
  }


  /**
   * Attempt to remove {@code u} from the group in which it is contained. Will
   * return false silently if {@code u} is not in any known group. Otherwise,
   * will forward the remove request to the {@code Group} that contains
   * {@code u}
   *
   * @param u
   *          the {@code User} to remove
   * @return true if the remove succeeded.
   */
  public boolean remove(User u) {
    Group group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    group.remove(u);
    userToUserGroup.remove(u);
    filterGroup(group);
    return true;
  }


  /**
   * Handle a websocket message from {@code u}. Will return false on error,
   * particularly if {@code u} is not contained in any known Group in this GCT.
   *
   * @param u
   *          the user that sent the message.
   * @param j
   *          the JsonObject message
   * @return boolean indicating the suceess of the message being processed.
   */
  public boolean message(User u, JsonObject j) {
    Group group = userToUserGroup.get(u);
    if (group == null) {
      return false;
    }
    return group.handleMessage(u, j);
  }


  // put a newly modified group into its proper set.
  private void filterGroup(Group g) {
    if (g.isFull()) {
      full.add(g);
      pending.remove(g);
    } else if (g.isEmpty()) {
      pending.remove(g);
      full.remove(g);
    } else { // not full game, some players.
      pending.add(g);
      full.remove(g);
    }
    GroupViewWebsocket.reportChange(openGroups());
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
