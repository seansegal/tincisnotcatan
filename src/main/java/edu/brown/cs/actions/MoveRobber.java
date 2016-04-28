package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Tile;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class MoveRobber implements FollowUpAction {

  private boolean _isSetup;
  private Referee _ref;
  private int _playerID;
  private HexCoordinate _newLocation;
  public final static String ID = "moveRobber";

  public MoveRobber(int playerID) {
    _playerID = playerID;
    _isSetup = false;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if(! _isSetup){
      throw new UnsupportedOperationException("An unsetup action cannot be executed");
    }
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    Set<Player> playersOnTile = null;
    try {
      playersOnTile = _ref.getBoard().moveRobber(_newLocation);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "Please choose a new location", null);
      toRet.put(_playerID, toAdd);
      return toRet;
    }
    for (Tile t : _ref.getBoard().getTiles()) {
      if (t.getCoordinate().equals(_newLocation)) {
        // TODO: Ansel finish Action.
      }
    }
    _ref.removeFollowUp(this);
    FollowUpAction followUp = new TakeCardAction(); // TODO!
    _ref.addFollowUp(ImmutableList.of(followUp));
    ActionResponse toAdd = new ActionResponse(true,
        "Please choose a new location", playersOnTile);
    toRet.put(_playerID, toAdd);
    return toRet;
  }

  @Override
  public JsonObject getData() {
    return null;
  }

  @Override
  public String getID() {
    return MoveRobber.ID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject json) {
    JsonObject params = json.get("newLocation").getAsJsonObject();
    HexCoordinate newLocation = convertToHexCoordrinate(params);
    assert ref != null;
    assert _playerID == playerID;
    _ref = ref;
    if (_ref.getPlayerByID(playerID) == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _newLocation = newLocation;
    _isSetup = true;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  private HexCoordinate convertToHexCoordrinate(JsonObject json) {
    try {
      int x = json.get("x").getAsInt();
      int y = json.get("y").getAsInt();
      int z = json.get("z").getAsInt();
      return new HexCoordinate(x, y, z);
    } catch (NullPointerException | JsonSyntaxException e) {
      throw new IllegalArgumentException("Missing coordinate x,y, or z");
    }
  }

}
