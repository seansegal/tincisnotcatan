package edu.brown.cs.api;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.Group;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;


public class GameOverProcessor implements RequestProcessor {

  @Override
  public boolean run(User user, Group g, JsonObject json,
      API api) {
    for (User u : g.connectedUsers()) {
      u.message(json);
    }
    System.out.println("GAMEOVERPROCESSED : " + json);
    g.clear();
    return true;
  }


  @Override
  public boolean match(JsonObject json) {
    return json.has("requestType")
        && json.get("requestType").getAsString().equals("gameOver");
  }

}
