package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.catan.Referee;

public class StartGameSetup implements Action {

  private Referee _ref;
  public static final String ID = "startSetup";

  public StartGameSetup(Referee ref) {
    assert ref != null;
    _ref = ref;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    List<Integer> turnOrder = _ref.getTurnOrder();
    for (int id : turnOrder) {
      Collection<FollowUpAction> followUp = new ArrayList<>();
      followUp.add(new PlaceInitialSettlement(id, 1));
      _ref.addFollowUp(followUp);
      followUp = new ArrayList<>();
      followUp.add(new PlaceRoad(id));
      _ref.addFollowUp(followUp);
    }
    Collections.reverse(turnOrder);
    for (int id : turnOrder) {
      Collection<FollowUpAction> followUp = new ArrayList<>();
      followUp.add(new PlaceInitialSettlement(id, 2));
      _ref.addFollowUp(followUp);
      followUp = new ArrayList<>();
      followUp.add(new PlaceRoad(id));
      _ref.addFollowUp(followUp);
    }
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    _ref.getPlayers().forEach(
        (player) -> {
          toReturn.put(player.getID(), new ActionResponse(true,
              "Let the games begin!", null));
        });
    return toReturn;
  }

}
