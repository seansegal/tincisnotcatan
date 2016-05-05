package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Referee.GameStatus;

public class StartGame implements Action {

  public static final String ID = "startGame";
  private Referee _ref;

  public StartGame(Referee ref) {
    _ref = ref;
    if(_ref.getGameStatus() != GameStatus.WAITING){
      throw new IllegalArgumentException("The game has already started");
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    JsonArray turnOrder = new JsonArray();
    for (int id : _ref.getTurnOrder()) {
      turnOrder.add(new JsonPrimitive(id));
    }
    int firstToGo = _ref.getTurnOrder().get(0);
    _ref.getPlayers().forEach(
        (player) -> {
          JsonObject json = new JsonObject();
          json.add("turnOrder", turnOrder);
          json.addProperty("isFirst",
              firstToGo == player.getID());
          toReturn.put(player.getID(), new ActionResponse(true, "", json));
        });
    _ref.addFollowUp(ImmutableList.of(new StartGameSetup(firstToGo)));
    _ref.setGameStatus(GameStatus.SETUP);
    return toReturn;
  }
}
