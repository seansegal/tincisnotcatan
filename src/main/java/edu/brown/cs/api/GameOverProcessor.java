package edu.brown.cs.api;

import java.util.Collection;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;


public class GameOverProcessor implements RequestProcessor {

  @Override
  public boolean run(User user, Collection<User> group, JsonObject json,
      API api) {
    for(User u : group) {
      u.message(json);
    }

    // TODO: any cleanup for game over!

    return true;
  }


  @Override
  public boolean match(JsonObject json) {
    return json.has("requestType") && json.get("requestType").getAsString().equals("gameOver");
  }

}
