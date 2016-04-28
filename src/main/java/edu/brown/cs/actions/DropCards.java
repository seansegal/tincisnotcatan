package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class DropCards implements FollowUpAction {

  private Referee _ref;
  private Player _player;
  private Map<Resource, Double> _toDrop;
  private final static double TOLERANCE = 0.001;
  public final static String ID = "dropCards";

  public DropCards(Referee ref, int playerID, JsonObject toDrop) {
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException("No player exists with the given ID.");
    }
    try {
      _toDrop = new HashMap<Resource, Double>();
      for (Resource res : Resource.values()) {
        if (res != Resource.WILDCARD) {
          _toDrop.put(res, toDrop.get(res.toString()).getAsDouble());
        }
      }
    } catch (JsonSyntaxException | NullPointerException e) {
      throw new IllegalArgumentException("Missing a resource or bad JSON input");
    }

  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // Validation:
    double numToDrop = _ref.getTurn().getMustDiscard(_player.getID());
    JsonObject toDrop = new JsonObject();
    toDrop.addProperty("numToDrop", numToDrop);
    double droppedCards = 0.0;
    for (Map.Entry<Resource, Double> res : _toDrop.entrySet()) {
      if (!_player.hasResource(res.getKey(), res.getValue())) {
        return ImmutableMap.of(_player.getID(), new ActionResponse(false,
            "You do not have the cards you are attempting to drop.", toDrop));
      }
      droppedCards += res.getValue();
    }
    if (Math.abs(droppedCards - _ref.getTurn().getMustDiscard(_player.getID())) > TOLERANCE) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You did not drop enough cards. Please try again.", toDrop));
    }

    // Action:
    for (Map.Entry<Resource, Double> res : _toDrop.entrySet()) {
      _player.removeResource(res.getKey(), res.getValue());
    }
    _ref.removeFollowUp(_player.getID(), DropCards.ID);

    // Formulate Responses:
    ActionResponse toPlayer = new ActionResponse(true, "Thanks for discarding",
        null);
    String message = String
        .format("%s finished discarding.", _player.getName());
    ActionResponse toAll = new ActionResponse(true, message, null); // TODO:
                                                                    // send
                                                                    // moveRovver
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.equals(_player)) {
        toReturn.put(p.getID(), toPlayer);
      } else {
        toReturn.put(p.getID(), toAll);
      }
    }
    return toReturn;
  }

}
