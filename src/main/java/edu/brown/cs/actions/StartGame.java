package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
          toReturn.put(player.getID(), new ActionResponse(true, "Let the games begin!", json));
        });

    List<Integer> setupOrder = _ref.getSetup().getSetupOrder();
    Set<Integer> placedFirstSettlement = new HashSet<>();
    int i = 0;
    for (int id : setupOrder) {
      Collection<FollowUpAction> followUp = new ArrayList<>();
      if (!placedFirstSettlement.contains(id)) {
        followUp.add(new PlaceInitialSettlement(id, 1));
        placedFirstSettlement.add(id);
      } else {
        followUp.add(new PlaceInitialSettlement(id, 2));
      }
      _ref.addFollowUp(followUp);
      followUp = new ArrayList<>();
      if (i == setupOrder.size() - 1) {
        followUp.add(new PlaceRoad(id, true));
      } else {
        followUp.add(new PlaceRoad(id, false));
      }
      _ref.addFollowUp(followUp);
      i++;
    }
    _ref.setGameStatus(GameStatus.SETUP);
    return toReturn;
  }
}
