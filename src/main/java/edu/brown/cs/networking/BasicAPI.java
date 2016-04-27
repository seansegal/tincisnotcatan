package edu.brown.cs.networking;

import java.util.Map;

import com.google.gson.JsonObject;

// for demo purposes: an implementation of an API that prints out its calls.
class BasicAPI implements API {

  @Override
  public JsonObject getGameState(int forPlayer) {
    System.out.println("DEMO: Called getGameState for player " + forPlayer);
    return null;
  }


  @Override
  public Map<Integer, JsonObject> performAction(String action) {
    System.out.println("DEMO: Called performAction for action " + action);
    return null;
  }


  @Override
  public int addPlayer(String playerAttributes) {
    System.out.println("DEMO: Called addPlayer for player " + playerAttributes);
    return 0;
  }

}
