package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlayMonopoly implements Action {
  private Referee _ref;
  private Player _player;
  private Resource _res;

  public PlayMonopoly(Referee ref, int playerID, Resource res) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _res = res;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    try {
      _player.playDevelopmentCard(DevelopmentCard.MONOPOLY);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "You don't have a Monopoly card", null);
      Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }
    int totalResCount = 0;
    for (Player otherPlayer : _ref.getPlayers()) {

    }

    return null;
  }

}
