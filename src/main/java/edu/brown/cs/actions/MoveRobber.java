package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Tile;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class MoveRobber implements FollowUpAction {
  private Referee _ref;
  private Player _player;
  private HexCoordinate _newLocation;
  public final static String ID = "moveRobber";

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
    Set<Player> playersOnTile = null;
    try {
      playersOnTile = _ref.getBoard().moveRobber(_newLocation);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "Please choose a new location", null);
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }
    for (Tile t : _ref.getBoard().getTiles()) {
      if (t.getCoordinate().equals(_newLocation)) {
        // TODO: Ansel finish Action.
      }
    }
    _ref.removeFollowUp(_player.getID(), MoveRobber.ID);
    Map<Integer, String> followUp = ImmutableMap.of(_player.getID(),
        TakeCardAction.ID);
    _ref.addFollowUp(followUp);
    ActionResponse toAdd = new ActionResponse(true,
        "Please choose a new location", playersOnTile);
    toRet.put(_player.getID(), toAdd);
    return toRet;
  }

}
