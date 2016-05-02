package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import edu.brown.cs.catan.Referee;

public class StartGame implements Action {

  public static final String ID = "startGame";
  private Referee _ref;

  public StartGame(Referee ref) {
    _ref = ref;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    JsonObject json = new JsonObject();
    JsonArray turnOrder = new JsonArray();
    for (int id : _ref.getTurnOrder()) {
      turnOrder.add(new JsonPrimitive(id));
    }
    json.add("turnOrder", turnOrder);
    json.add("isFirst", new JsonPrimitive(false));
    _ref.getPlayers().forEach(
        (player) -> {
          json.remove("isFirst");
          json.addProperty("isFirst",
              _ref.getTurnOrder().get(0) == player.getID());
          toReturn.put(player.getID(), new ActionResponse(true, "", json));
        });
    return toReturn;
  }
}
