package edu.brown.cs.actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

/**
 * Action responsible for trading with the bank.
 *
 * @author anselvahle
 *
 */
public class TradeWithBank implements Action {

  public static final String ID = "tradeWithBank";
  private Referee _ref;
  private Player _player;
  private Resource _toGive;
  private Resource _toGet;
  private double _amount;

  public TradeWithBank(Referee ref, int playerID, JsonObject params) {
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException("No player exists with the given ID.");
    }
    if (!params.get("toGive").isJsonNull() && !params.get("toGet").isJsonNull()) {
      String toGive = params.get("toGive").getAsString();
      String toGet = params.get("toGet").getAsString();
      _amount = params.get("amount").getAsDouble();
      _toGive = Resource.stringToResource(toGive);
      _toGet = Resource.stringToResource(toGet);
    } else {
      throw new IllegalArgumentException("toGive and toGet cannot be null.");
    }

  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    Map<Resource, Double> rates = _ref.getBankRates(_player.getID());
    Double rate = rates.get(_toGive);
    if (!_ref.getGameSettings().isDecimal) {
      rate = Math.ceil(rate);
    }
    double amountToGive = rate*_amount;
    if (_player.getResources().get(_toGive) < amountToGive) {
      String message = String.format(
          "You do not have enough %s to trade with the bank",
          _toGive.toString());
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          message, null));
    }
    if (!_ref.currentPlayer().equals(_player)) {
      String message = String.format(
          "You can only trade with the bank on your turn", _toGive.toString());
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          message, null));
    }

    // Action:
    _player.removeResource(_toGive, _amount*rate, _ref.getBank());
    _player.addResource(_toGet, _amount*1, _ref.getBank());

    // Format responses:
    NumberFormat nf = new DecimalFormat("##.##");
    String messageToPlayer = String.format(
        "You traded with the bank and got %s %s", nf.format(_amount), _toGet);
    ActionResponse respToPlayer = new ActionResponse(true, messageToPlayer,
        null);
    String messageToAll = String
        .format("%s traded %s %s for %s %s", _player.getName(), nf.format(amountToGive),
            _toGive, nf.format(_amount), _toGet);
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
