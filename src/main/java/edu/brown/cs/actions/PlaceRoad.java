package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.board.Path;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Referee.GameStatus;

/**
 * Action responsible for Placing roads during game play.
 * 
 * @author anselvahle
 *
 */
public class PlaceRoad implements FollowUpAction {

  private static final String ID = "placeRoad";
  private int _playerID;
  private boolean _isSetup;
  private Referee _ref;
  private IntersectionCoordinate _start;
  private IntersectionCoordinate _end;
  private static final String VERB = "place a road";
  private boolean _isFinal;

  public PlaceRoad(int playerID, boolean isFinal) {
    _playerID = playerID;
    _isSetup = false;
    _isFinal = isFinal;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!_isSetup) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before it is executed.");
    }

    // TODO: validate based on isGameSetup or based on canPlaceRoad!!!

    // Build the road
    new BuildRoad(_ref, _playerID, _start, _end, false).execute();
    _ref.removeFollowUp(this);

    boolean canPlace = false;
    for (Path p : _ref.getBoard().getPaths().values()) {
      if (p.canPlaceRoad(_ref.getPlayerByID(_playerID))) {
        canPlace = true;
        break;
      }
    }

    if (!canPlace && _ref.getNextFollowUp(_playerID) != null) {
      _ref.removeFollowUp(new PlaceRoad(_playerID, false));
      return ImmutableMap.of(_playerID, new ActionResponse(false,
          "There is nowhere for you to build a second road", null));
    }

    // Format responses:
    String messageToAll = String.format("%s placed a road.", _ref
        .getPlayerByID(_playerID).getName());
    ActionResponse respToPlayer = new ActionResponse(true,
        "You placed a road.", null);
    ActionResponse respToAll = new ActionResponse(true, messageToAll, null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.getID() == _playerID) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToAll);
      }
    }
    _ref.getSetup().nextTurn();
    if (_isFinal) {
      _ref.setGameStatus(GameStatus.PROGRESS);
      _ref.addFollowUp(ImmutableList
          .of(new RollDice(_ref.getTurnOrder().get(0))));
    }
    return toReturn;
  }

  @Override
  public JsonObject getData() {
    JsonObject json = new JsonObject();
    json.addProperty("message", "Please place a road.");
    return json;
  }

  @Override
  public String getID() {
    return PlaceRoad.ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {
    _ref = ref;
    if (playerID != _playerID) {
      throw new IllegalArgumentException("Wrong player.");
    }
    _start = toIntersectionCoordinate(params.get("start").getAsJsonObject());
    _end = toIntersectionCoordinate(params.get("end").getAsJsonObject());
    _isSetup = true;
  }

  private IntersectionCoordinate toIntersectionCoordinate(JsonObject object) {
    JsonObject coord1 = object.get("coord1").getAsJsonObject();
    JsonObject coord2 = object.get("coord2").getAsJsonObject();
    JsonObject coord3 = object.get("coord3").getAsJsonObject();
    HexCoordinate h1 = new HexCoordinate(coord1.get("x").getAsInt(), coord1
        .get("y").getAsInt(), coord1.get("z").getAsInt());
    HexCoordinate h2 = new HexCoordinate(coord2.get("x").getAsInt(), coord2
        .get("y").getAsInt(), coord2.get("z").getAsInt());
    HexCoordinate h3 = new HexCoordinate(coord3.get("x").getAsInt(), coord3
        .get("y").getAsInt(), coord3.get("z").getAsInt());
    return new IntersectionCoordinate(h1, h2, h3);
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
    PlaceRoad other = (PlaceRoad) obj;
    if (_playerID != other._playerID)
      return false;
    return true;
  }

}
