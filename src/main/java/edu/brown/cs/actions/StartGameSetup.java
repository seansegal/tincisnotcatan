package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    // List<Integer> turnOrder = new ArrayList<>(_ref.getTurnOrder());
    // for (int id : turnOrder) {
    // Collection<FollowUpAction> followUp = new ArrayList<>();
    // followUp.add(new PlaceInitialSettlement(id, 1));
    // _ref.addFollowUp(followUp);
    // followUp = new ArrayList<>();
    // followUp.add(new PlaceRoad(id, false));
    // _ref.addFollowUp(followUp);
    // }
    // Collections.reverse(turnOrder);
    // for (int id : turnOrder) {
    // Collection<FollowUpAction> followUp = new ArrayList<>();
    // followUp.add(new PlaceInitialSettlement(id, 2));
    // _ref.addFollowUp(followUp);
    // followUp = new ArrayList<>();
    // if(id == turnOrder.get(turnOrder.size() -1)){
    // followUp.add(new PlaceRoad(id, true));
    // }else{
    // followUp.add(new PlaceRoad(id, false));
    // }
    // _ref.addFollowUp(followUp);
    // }

    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    _ref.getPlayers().forEach(
        (player) -> {
          toReturn.put(player.getID(), new ActionResponse(true,
              "Let the games begin!", null));
        });
    return toReturn;
  }

}
