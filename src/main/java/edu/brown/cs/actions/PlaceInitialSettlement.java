package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Intersection;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlaceInitialSettlement implements FollowUpAction {

  private static final String ID = "placeSettlement";
  private int _playerID;
  private boolean _isSetup;
  private int _settlementNum;
  private Referee _ref;
  private Intersection _intersection;
  private static final String VERB = "place a Settlement";

  public PlaceInitialSettlement(int playerID, int settlementNum) {
    _playerID = playerID;
    _isSetup = false;
    _settlementNum = settlementNum;
    assert settlementNum == 1 || settlementNum == 2; // Only supporting placing
                                                     // the first two
                                                     // settlements.
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    if (!_isSetup) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before it is executed.");
    }
    Player player = _ref.getPlayerByID(_playerID);
    if (!_intersection.canPlaceSettlement(_ref)) {
      return ImmutableMap.of(player.getID(), new ActionResponse(false,
          "You cannot build a Settlement at that location.", null));
    }

    // The Action:
    player.useSettlement();
    _intersection.placeSettlement(player);
    _ref.removeFollowUp(this);

    ActionResponse respToPlayer = null;
    ActionResponse respToRest = null;
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    switch (_settlementNum) {
    case 1:
      respToPlayer = new ActionResponse(true,
          "You placed your first Settlement.", null);
      String message1 = String.format("%s placed their first Settlement.",
          player.getName());
      respToRest = new ActionResponse(true, message1, null);
      break;
    case 2:
      respToPlayer = new ActionResponse(
          true,
          "You placed your second Settlement. You've received your initial resources based on this Settlement.",
          null);
      String message2 = String.format("%s placed their second Settlement.",
          player.getName());
      respToRest = new ActionResponse(true, message2, null);
      HexCoordinate coord1 = _intersection.getPosition().getCoord1();
      HexCoordinate coord2 = _intersection.getPosition().getCoord2();
      HexCoordinate coord3 = _intersection.getPosition().getCoord3();
      List<Resource> resToAdd = new ArrayList<>();
      _ref.getBoard()
          .getTiles()
          .forEach(
              (tile) -> {
                if (tile.getCoordinate().equals(coord1)
                    || tile.getCoordinate().equals(coord2)
                    || tile.getCoordinate().equals(coord3)) {
                  if (tile.getType().getType() != null) {
                    resToAdd.add(tile.getType().getType());

                  }
                }
              });
      resToAdd.forEach((res) -> {
        player.addResource(res);
      });
      break;
    default:
      return Collections.emptyMap();
    }

    // Formulate responses and send:
    for (Player p : _ref.getPlayers()) {
      if (p.equals(player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else {
        toReturn.put(p.getID(), respToRest);
      }
    }
    return toReturn;
  }

  @Override
  public JsonObject getData() {
    JsonObject json = new JsonObject();
    json.addProperty("message", "Please place a Settlement");
    return json;
  }

  @Override
  public String getID() {
    return PlaceInitialSettlement.ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject params) {

    _isSetup = true;
  }

  @Override
  public String getVerb() {
    return VERB;
  }

}
