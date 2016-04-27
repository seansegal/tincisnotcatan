package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class MoveRobber implements Action {
  private Referee _ref;
  private Player _player;
  private HexCoordinate _newLocation;

  public MoveRobber(Referee ref, int playerID, HexCoordinate newLocation) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _newLocation = newLocation;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    try {
      _ref.getBoard().moveRobber(_newLocation);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "Please choose a new location", null);
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }
    // TODO waiting on FollowUpResponse Class from Sean
    return null;
  }
}
