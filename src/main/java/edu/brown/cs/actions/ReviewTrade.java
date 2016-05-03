package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

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

  public ReviewTrade(int playerID,
      Map<Resource, Double> resources) {
    _playerID = playerID;
    _resources = resources;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetUp) {
      throw new IllegalArgumentException();
    }
    Map<Integer, ActionResponse> toRet = new HashMap<>();
    String message = "";
    if (_acceptedTrade) {
      message = "You accepted the trade";
    } else {
      message = "You declined the trade";
    }
    ActionResponse toAdd = new ActionResponse(true, message, _acceptedTrade);
    toRet.put(_player.getID(), toAdd);
    _ref.removeFollowUp(this);
    return toRet;
  }

  @Override
  public JsonObject getData() {
    String message = "Please review the proposed trade and decide"
        + " whether or not to accept the offer.";
    JsonObject toRet = new JsonObject();
    toRet.addProperty("message", message);
    String trade = gson.toJson(_resources);
    toRet.addProperty("trade", trade);
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
    _acceptedTrade = params.get("acceptedTrade").getAsBoolean();
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
