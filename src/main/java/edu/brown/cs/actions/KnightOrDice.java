package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class KnightOrDice implements FollowUpAction {
  private Player _player;
  private final int _playerID;
  private Referee _ref;
  private static final String VERB = "start the next turn";
  private static final String ID = "knightOrDice";
  private boolean _isSetUp = false;
  private boolean _choseKnight = false;

  public KnightOrDice(int playerID) {
    _playerID = playerID;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetUp) {
      throw new IllegalArgumentException();
    }
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    if (!_choseKnight) {
      toRet = new RollDice(_ref, _playerID).execute();
    } else {
      toRet = new PlayKnight(_ref, _playerID, true).execute();
//      _ref.addFollowUp(ImmutableList.of(new RollDice(_playerID)));
    }
    _ref.removeFollowUp(this);
    return toRet;
  }

  @Override
  public JsonObject getData() {
    JsonObject toRet = new JsonObject();
    String message = "Please choose to roll the dice or play a knight.";
    toRet.addProperty("message", message);
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
    if (playerID != _playerID) {
      throw new IllegalArgumentException();
    }
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    if (!ref.currentPlayer().equals(_player)) {
      throw new IllegalArgumentException();
    }
    try {
      _choseKnight = params.get("choseKnight").getAsBoolean();
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("params must contains \"choseKnight\"");
    }
    _isSetUp = true;
  }

  @Override
  public String getVerb() {
    return VERB;
  }

}
