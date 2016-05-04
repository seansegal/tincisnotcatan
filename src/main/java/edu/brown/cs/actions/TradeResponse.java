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

public class TradeResponse implements FollowUpAction {
  private Player _player;
  private Player _tradee;
  private final int _playerID;
  private Referee _ref;
  private static final String VERB = "finalize the trade.";
  private static final String ID = "tradeResponse";
  private boolean _isSetUp = false;
  private Map<Resource, Double> _resources;
  private boolean _acceptedTrade = false;
  private final Gson gson = new Gson();
  private final Trade _trade;

  public TradeResponse(int playerID, Map<Resource, Double> resources,
      Trade trade) {
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
    // On cancel
    if(!_acceptedTrade) {
      for (Player p : _ref.getPlayers()) {
        if (!p.equals(_player)) {
          _ref.removeFollowUp(new ReviewTrade(p.getID(), _resources, _trade));
          String message = String.format("%s canceled the trade.",
              _player.getName());
          ActionResponse toAdd = new ActionResponse(true, message, null);
          toRet.put(p.getID(), toAdd);
        } else {
          String message = "You canceled the trade.";
          ActionResponse toAdd = new ActionResponse(true, message, null);
          toRet.put(p.getID(), toAdd);
        }
      }
      return toRet;
    }

    // On accept
    for (Resource res : _resources.keySet()) {
      if (_resources.get(res) < 0
          && _player.getResources().get(res) < Math.abs(_resources.get(res))) {
        ActionResponse toAdd = new ActionResponse(false, String.format(
            "You does not have enough %s to trade.", res), _resources);
        toRet.put(_player.getID(), toAdd);
        ActionResponse otherResponse = new ActionResponse(false, String.format(
            "%s does not have enough %s to trade.", _player.getID(), res),
            _resources);
        toRet.put(_tradee.getID(), otherResponse);
        return toRet;
      }
      if (_resources.get(res) > 0
          && _tradee.getResources().get(res) < _resources.get(res)) {
        ActionResponse toAdd = new ActionResponse(false, String.format(
            "You does not have enough %s to trade.", res), _resources);
        toRet.put(_tradee.getID(), toAdd);
        ActionResponse otherResponse = new ActionResponse(false, String.format(
            "%s does not have enough %s to trade.", _tradee.getID(), res),
            _resources);
        toRet.put(_player.getID(), otherResponse);
        return toRet;
      }
    }

    for (Resource res : _resources.keySet()) {
      _player.addResource(res, _resources.get(res));
      _tradee.removeResource(res, _resources.get(res));
    }
    ActionResponse toAdd = new ActionResponse(true, String.format(
        "You traded with %s.", _player.getName()), _resources);
    toRet.put(_tradee.getID(), toAdd);
    ActionResponse otherResponse = new ActionResponse(true, String.format(
        "You traded with %s.", _tradee.getID()), _resources);
    toRet.put(_player.getID(), otherResponse);

    return toRet;
  }

  @Override
  public JsonObject getData() {
    String message = "Please finalize the trade";
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
    if (_acceptedTrade) {
      Player trader = _ref.getPlayerByID(params.get("trader").getAsInt());
      if(!_player.equals(trader)) {
        throw new IllegalArgumentException();
      }
      _tradee = _ref.getPlayerByID(params.get("tradee").getAsInt());
    }
    _isSetUp = true;
  }

  @Override
  public String getVerb() {
    return VERB;
  }

}
