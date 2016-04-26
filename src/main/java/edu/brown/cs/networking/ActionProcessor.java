package edu.brown.cs.networking;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ActionProcessor implements RequestProcessor {

  private static final String IDENTIFIER = "action";
  private static final Gson GSON = new Gson();
  private final API api;


  public ActionProcessor(API api) {
    this.api = api;
  }

  @Override
  public boolean run(User<?> user, Collection<User<?>> group, JsonObject json) {
    json.add("player", GSON.toJsonTree(String.valueOf(user.userID())));
    System.out.println(json);

    Map<Integer, JsonObject> resp = api.performAction(json.toString());
    for (Integer i : resp.keySet()) {
      User<?> recipient = getUser(i, group);
      if (recipient == null) {
        System.out.format(
            "API thinks there's a player %d, but there isn't an active session.%n",
            i);
        continue;
      }
      json.add("content", resp.get(i));
      json.add("player", GSON.toJsonTree(i));
      System.out.println(i);
      System.out.println(json.get("requestType").getAsString());
      recipient.message(json);
    }
    return true;
  }

  @Override
  public String identifier() {
    return IDENTIFIER;
  }


  private User<?> getUser(int i, Collection<User<?>> users) {
    List<User<?>> list = users.stream().filter(u->u.userID()==i).collect(Collectors.toList());
    if (list.size() == 0) {
      return null;
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    assert false : "Non-unique identifying predicate!";
    return null;
  }

}
