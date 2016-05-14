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

/**
 * Action responsible for reviewing a trade.
 *
 * @author anselvahle
 *
 */
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
    if (_acceptedTrade) {
      for (Resource res : _resources.keySet()) {
        if (_resources.get(res) > 0
            && _player.getResources().get(res) < _resources.get(res)) {
          toRet = declineTrade(true);
          _ref.removeFollowUp(this);
          return toRet;
        }
      }
      toRet = acceptTrade();
    } else {
      toRet = declineTrade(false);
    }
    _ref.removeFollowUp(this);
    return toRet;
  }

  private Map<Integer, ActionResponse> declineTrade(boolean improperRes) {
    Map<Integer, ActionResponse> toRet = new HashMap<>();
    _trade.declinedTrade(_player.getID());
    boolean allDeclined = _trade.getDeclinedTrade().size() == _ref.getPlayers()
        .size() - 1;
    ReviewTradeResponse response = new ReviewTradeResponse(_acceptedTrade,
        allDeclined, _resources);
    if (improperRes) {
      ActionResponse otherResponse = new ActionResponse(true,
          "You do not have the proper resources to trade.", response);
      toRet.put(_player.getID(), otherResponse);
    } else {
      String message = "You declined the trade";
      ActionResponse toAdd = new ActionResponse(true, message, response);
      toRet.put(_player.getID(), toAdd);
    }
    for (Player p : _ref.getPlayers()) {
      if (p.equals(_ref.getPlayerByID(_trade.getTrader())) && allDeclined) {
        String message = "Everyone declined the trade";
        ActionResponse toAdd = new ActionResponse(true, message, response);
        toRet.put(p.getID(), toAdd);
        _ref.removeFollowUp(new TradeResponse(_trade.getTrader(), _resources,
            _trade));
      } else if (!p.equals(_player)) {
        String message = String.format("%s declined the trade",
            _player.getName());
        ActionResponse toAdd = new ActionResponse(true, message, response);
        toRet.put(p.getID(), toAdd);
      }
    }
    return toRet;
  }

  private Map<Integer, ActionResponse> acceptTrade() {
    Map<Integer, ActionResponse> toRet = new HashMap<>();
    _trade.acceptedTrade(_player.getID());
    boolean allDeclined = _trade.getDeclinedTrade().size() == _ref.getPlayers()
        .size() - 1;
    ReviewTradeResponse response = new ReviewTradeResponse(_acceptedTrade, allDeclined, _resources);
    for (Player p : _ref.getPlayers()) {
      if (!p.equals(_player)) {
        String message = String.format("%s accepted the trade.",
            _player.getName());
        ActionResponse forTrader = new ActionResponse(true, message, response);
        toRet.put(p.getID(), forTrader);
      } else {
        String message = "You accepted the trade";
        _trade.acceptedTrade(_player.getID());
        ActionResponse toAdd = new ActionResponse(true, message, response);
        toRet.put(_player.getID(), toAdd);

      }
    }
    return toRet;
  }

  private class ReviewTradeResponse {
    private boolean acceptedTrade;
    private boolean allDeclined;
    private Map<Resource, Double> resources;

    public ReviewTradeResponse(boolean acceptedTrade, boolean allDeclined,
        Map<Resource, Double> resources) {
      this.acceptedTrade = acceptedTrade;
      this.allDeclined = allDeclined;
      this.resources = resources;
    }
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
