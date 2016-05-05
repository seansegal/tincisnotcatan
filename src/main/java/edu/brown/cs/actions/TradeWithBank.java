package edu.brown.cs.actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class TradeWithBank implements Action {

  private Referee _ref;
  private Player _player;
  private Resource _toGive;
  private Resource _toGet;

  public TradeWithBank(Referee ref, int playerID, String toGive, String toGet) {
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException("No player exists with the given ID.");
    }
    _toGive = Resource.stringToResource(toGive);
    _toGet = Resource.stringToResource(toGet);
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    Map<Resource, Double> rates = _ref.getBankRates(_player.getID());
    Double rate = rates.get(_toGive);
    if (!_ref.getGameSettings().isDecimal) {
      rate = Math.ceil(rate);
    }
    if (_player.getResources().get(_toGive) < rate) {
      String message = String.format(
          "You do not have enough %s to trade with the bank",
          _toGive.toString());
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          message, null));
    }

    // Action:
    _player.removeResource(_toGive, rate, _ref.getBank());
    _player.addResource(_toGet, 1, _ref.getBank());


    // Format responses:
    String messageToPlayer = String.format(
        "You traded with the bank and got a %s", _toGet);
    ActionResponse respToPlayer = new ActionResponse(true, messageToPlayer,
        null);
    NumberFormat nf = new DecimalFormat("##.##");
    String messageToAll = String
        .format("%s traded %s %s for %s", _player.getName(), nf.format(rate),
            _toGive, _toGet.stringWithArticle());
    ActionResponse respToAll = new ActionResponse(true, messageToAll, null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.equals(_player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToAll);
      }
    }
    return toReturn;
  }
}
