package edu.brown.cs.api;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.Group;
import edu.brown.cs.networking.Networking;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;

public class ActionProcessor implements RequestProcessor {

  private static final String IDENTIFIER  = "action";
  private static final String REQUEST_KEY = "requestType";
  private static final Gson   GSON        = new Gson();


  @Override
  public boolean run(User user, Group g, JsonObject json,
      API api) {
    json.add("player", GSON.toJsonTree(String.valueOf(user.userID())));
    System.out.println(json);

    Map<Integer, JsonObject> resp = api.performAction(json.toString());
    for(User u : g.connectedUsers()) {
      if(resp.containsKey(u.userID())) {
        json.add("content", resp.get(u.userID()));
        json.add("player", Networking.GSON.toJsonTree(u.userID()));
        u.message(json);
        // and get game state
        JsonObject gs = api.getGameState(u.userID());
        gs.addProperty(REQUEST_KEY, "getGameState");
        u.message(gs);
      }
    }
    return true;
  }


  @Override
  public boolean match(JsonObject j) {
    if (j.has(REQUEST_KEY) && !j.get(REQUEST_KEY).isJsonNull()) {
      return j.get(REQUEST_KEY).getAsString().equals(IDENTIFIER);
    }
    return false;
  }

}
