package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class EndTurn implements Action {
  private Player _player;
  private Referee _ref;
  private boolean _hasKnight = false;

  public EndTurn(Referee ref, int playerID) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }

    if (_player.getDevCards().get(DevelopmentCard.KNIGHT) != 0) {
      _hasKnight = true;
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (_ref.currentPlayer().equals(_player)) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "It is not your turn.", null));
    }
    _ref.startNextTurn();
    Player nextPlayer = _ref.currentPlayer();
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();

    for (Player p : _ref.getPlayers()) {
      if (!p.equals(nextPlayer) && !p.equals(_player)) {
        String message = String.format(
            "%s just ended their turn. It is now %s's turn.",
            _player.getName(), nextPlayer.getName());
        ActionResponse toAdd = new ActionResponse(true, message, null);
        toRet.put(p.getID(), toAdd);
      } else if (!p.equals(nextPlayer) && p.equals(_player)) {
        String message = String.format(
            "You just ended your turn. It is now %s's turn.",
            nextPlayer.getName());
        ActionResponse toAdd = new ActionResponse(true, message, null);
        toRet.put(p.getID(), toAdd);
      } else {
        FollowUpAction toDoNext = null;
        if (!_hasKnight) {
          toDoNext = new RollDice(_player.getID());
        } else {
          toDoNext = new KnightOrDice(_player.getID());
        }
        Collection<FollowUpAction> followUp = new ArrayList<FollowUpAction>();
        followUp.add(toDoNext);
        _ref.addFollowUp(followUp);
        String message = String
            .format("%s just ended their turn. It is now your turn.",
                _player.getName());
        ActionResponse toAdd = new ActionResponse(true, message, null);
        toRet.put(p.getID(), toAdd);
      }
    }
    return toRet;
  }
}
