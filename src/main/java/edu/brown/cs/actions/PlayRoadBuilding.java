package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.brown.cs.board.Path;
import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class PlayRoadBuilding implements Action {

  private Referee _ref;
  private Player _player;

  public PlayRoadBuilding(Referee ref, int playerID) {
    assert ref != null;
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException(String.format(
          "No player exists with the id: %d", playerID));
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    if (!(_player.getDevCards().get(DevelopmentCard.ROAD_BUILDING) > 0)) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You don't have a Road Building card", null));
    }
    if (_ref.getTurn().devHasBeenPlayed()) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You already played a development card this turn", null));
    }
    if (!_ref.getTurn().hadInitialDevCard(DevelopmentCard.ROAD_BUILDING)) {
      return ImmutableMap
          .of(_player.getID(), new ActionResponse(false,
              "You cannot play a development card on the turn you bought it",
              null));
    }

    boolean canPlace = false;
    for (Path p : _ref.getBoard().getPaths().values()) {
      if (p.canPlaceRoad(_player)) {
        canPlace = true;
        break;
      }
    }

    if (!canPlace) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "There is nowhere for you to build a road", null));
    }

    try {
      _player.playDevelopmentCard(DevelopmentCard.ROAD_BUILDING);
    } catch (IllegalArgumentException e) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You don't have Road Building", null));
    }


    //Action:
    _ref.playDevCard();

    ActionResponse respToPlayer = new ActionResponse(true,
        "You played Road Building.", null);
    ActionResponse respToAll = new ActionResponse(true, String.format(
        "%s played Road Building", _player.getName()), null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.equals(_player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToAll);
      }
    }

    // Follow up PlaceRoad actions:
    _ref.addFollowUp(ImmutableList.of(new PlaceRoad(_player.getID(), false)));
    _ref.addFollowUp(ImmutableList.of(new PlaceRoad(_player.getID(), false)));

    return toReturn;

  }

}
