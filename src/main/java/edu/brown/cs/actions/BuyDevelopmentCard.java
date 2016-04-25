package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class BuyDevelopmentCard implements Action {

  private Referee _referee;
  private Player _player;

  public BuyDevelopmentCard(Referee ref, int playerID) {
    _referee = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_player.canBuyDevelopmentCard()) {
      ActionResponse resp = new ActionResponse(false,
          "You cannot afford a Development Card.", null);
      return ImmutableMap.of(_player.getID(), resp);
    }
    _player.buyDevelopmentCard();
    DevelopmentCard newCard = _referee.getDevCard();
    _player.addDevelopmentCard(newCard);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    // Formulate Responses:
    String messageToPlayer = String.format(
        "You bought a development card. It is %s", newCard.toString());
    ActionResponse toPlayer = new ActionResponse(true, messageToPlayer, null);
    String message = String.format("%s bought a development card.",
        _player.getName());
    ActionResponse toAll = new ActionResponse(true, message, null);
    for (Player player : _referee.getPlayers()) {
      if (player.equals(_player)) {
        toReturn.put(player.getID(), toPlayer);
      } else {
        toReturn.put(player.getID(), toAll);
      }
    }
    return toReturn;
  }
}
