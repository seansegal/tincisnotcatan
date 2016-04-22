package edu.brown.cs.actions;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Settings;

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
    if (_mustPay) {
      Map<Resource, Double> resources = _player.getResources();
      for (Map.Entry<Resource, Double> cost : Settings.SETTLEMENT_COST
          .entrySet()) {
        if (resources.get(cost.getKey()) < cost.getValue()) {
          return ImmutableMap.of(_player.getID(), new ActionResponse(false,
              "You do not have the resources to afford a Settlement.", null));
        }
      }
      _player.buildSettlement();
    }
    // TODO: add turn validation, add graph validation
    _player.useSettlement();
    _intersection.placeSettlement(_player);

    // TODO: formulate response & send.
    return null;
  }

}
