package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.Referee;

public class StartGameSetup implements FollowUpAction {

  private Referee _ref;
  private int _playerID;
  public static final String ID = "startSetup";
  private static final String VERB = "start the game";

  public StartGameSetup(int playerID) {
    _playerID = playerID;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Get the setupOrder
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

    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    _ref.getPlayers().forEach(
        (player) -> {
          toReturn.put(player.getID(), new ActionResponse(true,
              "Let the games begin!", null));
        });
    return toReturn;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {
    _ref = ref;
    if(_playerID != playerID){
      throw new IllegalArgumentException("Wrong player initiating the action.");
    }
  }

  @Override
  public JsonObject getData() {
    JsonObject json = new JsonObject();
    json.addProperty("message", "Start the game!");
    return json;
  }

  @Override
  public String getID() {
    return ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public String getVerb() {
    return VERB;
  }

}
