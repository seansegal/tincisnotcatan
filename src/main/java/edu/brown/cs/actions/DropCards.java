package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class DropCards implements Action {

  private Referee _ref;
  private Player _player;
  private Map<Resource, Double> _toDrop;

  public DropCards(Referee ref, int playerID, Map<String, Double> toDrop) {
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    if (_player == null) {
      throw new IllegalArgumentException("No player exists with the given ID.");
    }
    _toDrop = new HashMap<Resource, Double>();
    for (Map.Entry<String, Double> res : toDrop.entrySet()) {
      _toDrop.put(Resource.stringToResource(res.getKey()), res.getValue());
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    //TODO
    return null;
  }

}
