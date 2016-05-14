package edu.brown.cs.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

/**
 * Action Responsible for moving the robber.
 * 
 * @author anselvahle
 *
 */
public class MoveRobber implements FollowUpAction {

  private boolean _isSetup;
  private Referee _ref;
  private int _playerID;
  private HexCoordinate _newLocation;
  public final static String ID = "moveRobber";
  private final static String VERB = "move the Robber";
  private boolean _isTurnStart = false;
  private boolean _sevenWasRolled = false;

  public MoveRobber(int playerID) {
    _playerID = playerID;
    _isSetup = false;
  }

  public MoveRobber(int playerID, boolean isTurnStart) {
    _playerID = playerID;
    _isSetup = false;
    _isTurnStart = isTurnStart;
  }

  public MoveRobber(int playerID, boolean isTurnStart, boolean sevenWasRolled) {
    _playerID = playerID;
    _isSetup = false;
    _sevenWasRolled = sevenWasRolled;
  }


  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetup) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before it is executed.");
    }
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    Set<Integer> playersOnTile = Collections.emptySet();
    try {
      playersOnTile = _ref.getBoard().moveRobber(_newLocation);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "Please choose a new location", null);
      toRet.put(_playerID, toAdd);
      return toRet;
    }
    int originalPlayersOnTile = playersOnTile.size();
    // Remove the actual player & all players with no resources:
    Set<Integer> temp = new HashSet<>();
    for (int player : playersOnTile) {
      if (player != _playerID) {
        for (Map.Entry<Resource, Double> res : _ref.getPlayerByID(player)
            .getResources().entrySet()) {
          if (res.getValue() >= 1.0) {
            temp.add(player);
            break;
          }
        }
      } else {
        originalPlayersOnTile -= 1;
      }
    }
    playersOnTile = temp;
    _ref.removeFollowUp(this);
    if (!playersOnTile.isEmpty()) {
      FollowUpAction followUp = new TakeCardAction(_playerID, playersOnTile); // TODO!
      _ref.addFollowUp(ImmutableList.of(followUp));
    }
    if(_isTurnStart){
      _ref.addFollowUp(ImmutableList.of(new RollDice(_playerID)));
    }
    String message = "You moved the Robber. There was no one to steal from where you placed the Robber.";
    if (originalPlayersOnTile > 0) {
      message = "You moved the Robber. No player has enough cards for you to steal.";
    }
    ActionResponse toPlayer = new ActionResponse(true, message, playersOnTile);
    String messageToAll = String.format("%s moved the Robber", _ref
        .getPlayerByID(_playerID).getName());
    ActionResponse toAll = new ActionResponse(true, messageToAll, null);
    for (Player p : _ref.getPlayers()) {
      if (p.getID() == _playerID) {
        toRet.put(p.getID(), toPlayer);
      } else {
        toRet.put(p.getID(), toAll);
      }
    }
    return toRet;
  }

  @Override
  public JsonObject getData() {
    String message = "Please move the robber.";
    if(_sevenWasRolled){
      message = "A 7 was rolled. Please move the robber.";
    }

    JsonObject toReturn = new JsonObject();
    toReturn.addProperty("message", message);
    return toReturn;
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

  @Override
  public String getVerb() {
    return MoveRobber.VERB;
  }

}
