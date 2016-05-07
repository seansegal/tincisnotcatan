package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.print;

import java.util.Map;

import com.google.gson.JsonObject;

// for demo purposes: an implementation of an API that prints out its calls.
class BasicAPI implements API {

  @Override
  public JsonObject getGameState(int forPlayer) {
    print("DEMO: Called getGameState for player " + forPlayer);
    return null;
  }


  @Override
  public Map<Integer, JsonObject> performAction(String action) {
    print("DEMO: Called performAction for action " + action);
    return null;
  }


  @Override
  public int addPlayer(JsonObject playerAttributes) {
    print("DEMO: Called addPlayer for player " + playerAttributes);
    return 0;
  }


  @Override
  public void setSettings(JsonObject settings) {
    // TODO Auto-generated method stub

  }

}
