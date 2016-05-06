package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class UpdateResource implements Action {
  public static final String ID = "updateResource";
  private Player _player;
  private Referee _ref;

  public UpdateResource(Referee ref, int playerID) {
    assert ref != null;
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException(String.format(
          "No player exists with ID: %d", playerID));
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    Map<Resource, Double> resources = new HashMap<>();
    for (Resource res : _player.getResources().keySet()) {
      _player.removeResource(res, _player.getResources().get(res),
          _ref.getBank());
      _player.addResource(res, 99.0, _ref.getBank());
      resources.put(res, 99.0);
    }
    String message = "You unlocked the power!";
    ActionResponse toAdd = new ActionResponse(true, message, resources);
    return ImmutableMap.of(_player.getID(), toAdd);
  }

}
