package edu.brown.cs.networking;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.eclipse.jetty.websocket.api.Session;

import spark.Spark;

// Grand Central Terminal - Routes all of the inputs to appropriate groups
public class GCT {

  private static final GCT                              instance                =
      new GCT();

  private static final PriorityBlockingQueue<UserGroup> pending                 =
      new PriorityBlockingQueue<>();

  private static final List<UserGroup>                  full                    =
      Collections.synchronizedList(new ArrayList<>());

  private static final Map<Session, UserGroup>          groupMap                =
      new ConcurrentHashMap<>();

  private static final Map<Session, User>               sessionToUser           =
      new ConcurrentHashMap<>();

  private static Class<? extends API>                   apiClass;

  private static final int                              NEED_TO_GENERALIZE_THIS =
      2;

  private static int                                    GROUP_ID                =
      1;
  private static int                                    SESSION_ID              =
      1;


  public static GCT getInstance() {
    return instance;
  }


  public static void setAPI(Class<? extends API> api) {
    apiClass = api;
  }


  private GCT() {
    Spark.webSocket("/action", ReceivingHandler.class);
    Spark.init();
  }


  // add verification?? TODO
  public boolean register(Session s, List<HttpCookie> cookies) {
    User newUser = new User(s, cookies);
    sessionToUser.put(s, newUser);
    return add(newUser);
  }


  public boolean add(User u) {
    UserGroup group = pending.poll();

    if (group == null) {
      try {

        group = new UserGroup(
            Integer.valueOf(u.getField("numPlayersDesired")),
            String.valueOf(GROUP_ID++),
            apiClass.newInstance());

      } catch (InstantiationException | IllegalAccessException e) {
        System.out.println("Error : Failed to create a new API class");
        e.printStackTrace();
      }
      group.add(u);
      groupMap.put(u.session(), group);
    } else if (group.isFull()) {
      throw new IllegalStateException("Had a full UserGroup in pending PQ");
    } else {
      group.add(u);
      groupMap.put(u.session(), group);
    }

    if (group.isFull()) {
      full.add(group);
    } else {
      pending.add(group);
    }
    return true; // figure out what this boolean should really represent TODO
  }


  public boolean remove(Session s, int statusCode, String reason) {
    UserGroup group = groupMap.remove(s);

    if (group == null) {
      return false;
    }

    if (group.isFull()) {
      assert full.remove(group) : "This group should have been in full";
    } else {
      assert pending.remove(group) : "This group should have been in pending";
    }

    boolean different = group.remove(sessionToUser.get(s));

    if (different) {
      group.stampNow(); // groups that lose a user move to the back of the line.
    }
    if (!group.isEmpty()) {
      pending.add(group);
    }

    return different;

  }


  public boolean message(Session s, String message) {
    UserGroup sg = groupMap.get(s);
    if (sg == null) {
      return false;
    }
    return sg.message(sessionToUser.get(s), message);
  }


}
