package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Trade;

public class ReviewTrade implements FollowUpAction {
  private Player _player;
  private final int _playerID;
  private Referee _ref;
  private static final String VERB = "review the trade.";
  private static final String ID = "reviewTrade";
  private boolean _isSetUp = false;
  private boolean _acceptedTrade;
  private Map<Resource, Double> _resources;
  private Gson gson = new Gson();
  private final Trade _trade;

  public ReviewTrade(int playerID,
 Map<Resource, Double> resources, Trade trade) {
    _playerID = playerID;
    _resources = resources;
    _trade = trade;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetUp) {
      throw new IllegalArgumentException();
    }
    Map<Integer, ActionResponse> toRet = new HashMap<>();
    String message = "";
    String mForTrader = "";
    if (_acceptedTrade) {
      for (Resource res : _resources.keySet()) {
        if (_resources.get(res) < 0
            && _player.getResources().get(res) < Math.abs(_resources.get(res))) {
          ActionResponse otherResponse = new ActionResponse(false,
              "You do not have the proper resources to trade.",
              _resources);
          toRet.put(_player.getID(), otherResponse);
          return toRet;
        }
      }
      message = "You accepted the trade";
      _trade.acceptedTrade(_player.getID());
      mForTrader = String.format("%s accepted the trade.", _player.getName());
    } else {
      message = "You declined the trade";
      _trade.declinedTrade(_player.getID());
      mForTrader = String.format("%s declined the trade.", _player.getName());
    }
    ActionResponse toAdd = new ActionResponse(true, message, _acceptedTrade);
    toRet.put(_player.getID(), toAdd);
    ActionResponse forTrader = new ActionResponse(true, mForTrader, null);
    toRet.put(_trade.getTrader(), forTrader);
    _ref.removeFollowUp(this);
    return toRet;
  }

  @Override
  public JsonObject getData() {
    String message = "Please review the proposed trade and decide"
        + " whether or not to accept the offer.";
    JsonObject toRet = new JsonObject();
    toRet.addProperty("message", message);
    JsonElement trade = gson.toJsonTree(_trade);
    toRet.add("trade", trade);
    return toRet;
  }

  @Override
  public String getID() {
    return ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _acceptedTrade = params.get("tradeAccepted").getAsBoolean();
    _isSetUp = true;
  }

  @Override
  public String getVerb() {
    return VERB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + _playerID;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ReviewTrade other = (ReviewTrade) obj;
    if (_playerID != other._playerID)
      return false;
    return true;
  }

}
