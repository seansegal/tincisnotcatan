package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class BuildSettlement implements Action {

  private Player _player;
  private Intersection _intersection;
  private boolean _mustPay;
  private Referee _ref;

  public BuildSettlement(Referee ref, int playerID, IntersectionCoordinate i,
      boolean mustPay) {
    assert ref != null && i != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    _intersection = _ref.getBoard().getIntersections().get(i);
    _mustPay = mustPay;
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    if (_intersection == null) {
      throw new IllegalArgumentException("The intersection could not be found.");
    }

  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation, TODO: add turn validation
    if (_player.numSettlements() <= 0) {
      ActionResponse resp = new ActionResponse(false,
          "You do not have any more settlements.", null);
      return ImmutableMap.of(_player.getID(), resp);
    }
    if (_mustPay && !_player.canBuildSettlement()) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You do not have the resources to afford a Settlement.", null));
    }
    if (!_intersection.canPlaceSettlement(_ref)) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You cannot build a Settlement at that location.", null));
    }

    // The Action:
    if (_mustPay) {
      _player.buildSettlement();
    }
    _player.useSettlement();
    _intersection.placeSettlement(_player);

    // Formulate responses and send:
    ActionResponse respToBuyer = new ActionResponse(true,
        "Congratulations! You built a Settlement.", null);
    String message = String.format("%s built a settlement.", _player.getName());
    ActionResponse respToRest = new ActionResponse(true, message, null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player player : _ref.getPlayers()) {
      if (player.equals(_player)) {
        toReturn.put(player.getID(), respToBuyer);
      } else {
        toReturn.put(player.getID(), respToRest);
      }
    }
    return toReturn;
  }


}
