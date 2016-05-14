package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.board.Path;
import edu.brown.cs.board.PathCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Referee.GameStatus;

/**
 * Action responsible for building a road during game play.
 *
 * @author anselvahle
 *
 */
public class BuildRoad implements Action {

  public static final String ID = "buildRoad";
  private Referee _ref;
  private Player _player;
  private Path _path;
  private boolean _mustPay;

  public BuildRoad(Referee ref, int playerID, IntersectionCoordinate start,
      IntersectionCoordinate end, boolean mustPay) {
    assert ref != null && start != null && end != null;
    _mustPay = mustPay;
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    Intersection startPoint = ref.getBoard().getIntersections().get(start);
    Intersection endPoint = ref.getBoard().getIntersections().get(end);
    if (startPoint == null) {
      throw new IllegalArgumentException(
          "The starting coordinate does not exist");
    }
    if (endPoint == null) {
      throw new IllegalArgumentException(
"The ending coordinate does not exist");
    }
    _path = ref.getBoard().getPaths().get(new PathCoordinate(start, end));
    if (_path == null) {
      throw new IllegalArgumentException(
          "There is no path from the start coordinate to the end coordinate");
    }
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (_ref.getGameStatus() == GameStatus.PROGRESS
        && !_ref.currentPlayer().equals(_player)) {
      ActionResponse resp = new ActionResponse(false,
          "You cannot build when it is not your turn", null);
      return ImmutableMap.of(_player.getID(), resp);
    }
    if (!(_player.numRoads() > 0)) {
      ActionResponse resp = new ActionResponse(false,
          "You do no have nay more roads", null);
      return ImmutableMap.of(_player.getID(), resp);
    }
    if (_mustPay) {
      if (_player.canBuildRoad()) {
        _player.buildRoad();
      } else {
        ActionResponse resp = new ActionResponse(false,
            "You cannot afford to buy a road", null);
        return ImmutableMap.of(_player.getID(), resp);
      }

    }
    if (!_path.canPlaceRoad(_player)) {
      ActionResponse resp = new ActionResponse(false,
          "You cannot built a road in that location", null);
      return ImmutableMap.of(_player.getID(), resp);
    }
    _player.useRoad();
    _path.placeRoad(_player);

    ActionResponse toPlayer = new ActionResponse(true, "You built a Road",
        null);
    String message = String.format("%s built a Road", _player.getName());
    ActionResponse toAll = new ActionResponse(true, message, null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player player : _ref.getPlayers()) {
      if (player.equals(_player)) {
        toReturn.put(player.getID(), toPlayer);
      } else {
        toReturn.put(player.getID(), toAll);
      }
    }
    return toReturn;
  }
}
