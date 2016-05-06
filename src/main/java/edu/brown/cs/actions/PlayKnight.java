package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class PlayKnight implements Action {

  public static final String ID = "playKnight";
  private Player _player;
  private Referee _ref;
  private boolean _isTurnStart = false;

  public PlayKnight(Referee ref, int playerID) {
    assert ref != null;
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException(String.format(
          "No player exists with ID: %d", playerID));
    }
  }

  public PlayKnight(Referee ref, int playerID, boolean isTurnStart) {
    assert ref != null;
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException(String.format(
          "No player exists with ID: %d", playerID));
    }
    _isTurnStart = true;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    if (!(_player.getDevCards().get(DevelopmentCard.KNIGHT) > 0)) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You don't have a Knight", null));
    }
    if (_ref.getTurn().devHasBeenPlayed()) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You already played a development card this turn", null));
    }
    if (!_ref.getTurn().hadInitialDevCard(DevelopmentCard.KNIGHT)) {
      return ImmutableMap
          .of(_player.getID(), new ActionResponse(false,
              "You cannot play a development card on the turn you bought it",
              null));
    }
    try {
      _player.playDevelopmentCard(DevelopmentCard.KNIGHT);
    } catch (IllegalArgumentException e) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You don't have a Knight", null));
    }
    // Action:
    _ref.playDevCard();

    ActionResponse respToPlayer = new ActionResponse(true,
        "You played a Knight.", _player.numPlayedKnights());
    ActionResponse respToAll = new ActionResponse(true, String.format(
        "%s played a Knight.", _player.getName()), null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.equals(_player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToAll);
      }
    }
    if (_isTurnStart) {
      // Follow up with MoveRobber and RollDice
      _ref.addFollowUp(ImmutableList.of(new MoveRobber(_player.getID(), true)));
    } else {
      // Follow up MoveRobber action:
      _ref.addFollowUp(ImmutableList.of(new MoveRobber(_player.getID())));
    }

    return toReturn;
  }

}
