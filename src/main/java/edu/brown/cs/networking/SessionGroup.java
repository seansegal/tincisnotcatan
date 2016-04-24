package edu.brown.cs.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import edu.brown.cs.api.CatanAPI;

class SessionGroup implements Timestamped {

  private List<Session>  sessions;
  private int            size;
  private long           timestamp;
  private String         id;
  private Map<Session, Integer> intForSession;
  private Map<Integer, Session> sessionForInt;

  private final Gson     GSON = new Gson();
  private final CatanAPI api;



  public SessionGroup(int size, String id) {
    api = new CatanAPI();
    sessions = new ArrayList<>();
    intForSession = new HashMap<>();
    sessionForInt = new HashMap<>();
    this.size = size;
    this.timestamp = System.currentTimeMillis();
    this.id = id;
  }


  public boolean isFull() {
    return sessions.size() == size;
  }


  public boolean isEmpty() {
    return sessions.isEmpty();
  }


  // currently just add to list, can add notification functionality.
  public boolean add(Session s) {
    if (!isFull()) {
      System.out.format("Session %s was added to SessionGroup %s%n",
          s.getLocalAddress(), id);
      // TODO: Add player attributes here.
      intForSession.put(s, api.addPlayer(""));
      sessionForInt.put(intForSession.get(s), s);
      return sessions.add(s);
    }
    System.out.format("Session %s was not added to %s, it is full%n", s, id);
    return false;
  }


  // currently just removes from list, can add notification functionality.
  public boolean remove(Session s) {
    System.out.format("Session %s was removed from SessionGroup %s%n",
        s.getLocalAddress(), id);
    boolean toReturn = sessions.remove(s);
    if (isEmpty()) {
      System.out.format("SessionGroup %s is now empty.%n", id);
    }
    return toReturn;
  }


  public boolean message(Session s, String message) {
    if (!sessions.contains(s)) {
      System.out.format(
          "Error : attempted to handle message from %s by SessionGroup %s, not contained.%n",
          s.getLocalAddress(), id);
      return false;
    }
    System.out.format(
        "Session %s sent a message : %s through SessionGroup %s%n",
        s.getLocalAddress(), message, id);

    @SuppressWarnings("unchecked")
    Map<String, Object> rawMap = GSON.fromJson(message, Map.class);
    Request req = new Request(rawMap);
    if (req.isValid()) {
      switch (req.type()) {
        case CHAT:
          return handleChatMessage(s, req.content());
        case ACTION:
          System.out.format("Action requested : %s%n", req.content());
          handleAction(s, req.content());
          return true;
        default:
          System.out.format("Session %s made an illegal request : %s%n",
              s.getLocalAddress(), message);
          return false;
      }
    }
    return false;
  }


  private boolean handleAction(Session s, Object content) {
    Map<?, ?> map = (Map<?, ?>) content;
    String methodName = (String) map.get("methodName");
    List<?> argsMaybe = (List<?>) map.get("args");
    if (argsMaybe.isEmpty()) {
      System.out.println("No args given,");
    } else {
      System.out.println("Args : " + argsMaybe.toString());
    }
    JSONObject toSend = new JSONObject();
    Object toRet;
    try {
      toSend.put("responseType", methodName);
      switch (methodName) {
        case "getBoard":
          toRet = GSON.fromJson(api.getBoard(), Map.class);
          toSend.put("content", toRet);
          Broadcast.toSession(s, toSend.toString());
          break;
        case "getPlayers":
          toRet = GSON.fromJson(api.getPlayers(), Map.class);
          toSend.put("content", toRet);
          Broadcast.toSession(s, toSend.toString());
          break;
        case "getGameState" :
          toRet = GSON.fromJson(api.getGameState(intForSession.get(s)), Map.class);
          toSend.put("content", toRet);
          Broadcast.toSession(s, toSend.toString());
          break;
        case "performAction" :
          // TODO: see how hans sends this info over, react accordingly.
          Map<Integer, String> resp = api.performAction((String) map.get("content"));
          for(Integer i : resp.keySet()) {
            toSend.put("content", resp.get(i));
            Broadcast.toSession(sessionForInt.get(i), toSend.toString());
          }
          break;
        default:
          throw new IllegalArgumentException(
              "Unsupported action : " + methodName);
      }
    } catch (JSONException j) {
      j.printStackTrace();
    }


    return true;
  }


  private boolean handleChatMessage(Session s, Object message) {

    String cast;
    try {
      cast = (String) message;
    } catch (ClassCastException e) {
      System.out.format(
          "ClassCastException: Session %s sent %s through /chat%n",
          s.getLocalAddress(), message);
      return false;
    }

    System.out.println("Message processed : " + cast);
    Broadcast.toAll(sessions,
        Chat.createMessage(s.getLocalAddress().toString(), cast, userIds())
            .toString());
    return true;
  }


  // TEMPORARY
  public Collection<String> userIds() {
    Collection<String> toRet = new ArrayList<>();
    for (Session s : sessions) {
      toRet.add(s.getLocalAddress().toString());
    }
    return toRet;
  }


  public boolean contains(Session s) {
    return sessions.contains(s);
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
