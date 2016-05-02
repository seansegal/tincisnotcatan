package edu.brown.cs.api;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class WaitingOnActionException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private int _playerID = -1;
  private Referee _ref;
  private final String _messageForPlayer;
  private final String _messageForAll;

  public WaitingOnActionException(String verb, int playerID, Referee ref) {
    super("WAITING: " + verb);
    _messageForPlayer = String.format(
        "You must %s before perfoming other actions", verb);
    _messageForAll = String.format("Waiting on %s to %s...",
        ref.getPlayerByID(playerID).getName(), verb);
    _playerID = playerID;
    _ref = ref;

  }

  public Map<Integer, ActionResponse> getResponses() {
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    ActionResponse respToPlayer = new ActionResponse(true, _messageForPlayer,
        null);
    ActionResponse respToAll = new ActionResponse(true, _messageForAll, null);
    for (Player player : _ref.getPlayers()) {
      if (player.getID() == _playerID) {
        toReturn.put(player.getID(), respToPlayer);
      } else {
        toReturn.put(player.getID(), respToAll);
      }
    }
    return toReturn;
  }

}
