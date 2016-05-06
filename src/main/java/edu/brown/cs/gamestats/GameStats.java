package edu.brown.cs.gamestats;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Game Stats for one game of Catan. Cannot be intantiated
 * outside of the package. Users should use the CatanStats getGameStatsObject
 * method in order to get an instance of this object.
 *
 */
public class GameStats {

  private Map<Integer, Integer> _rolls;
  private boolean _cheatingUsed = false;

  GameStats() {
    _rolls = new HashMap<>();
  }

  public void addRoll(int roll) {
    if (_rolls.containsKey(roll)) {
      _rolls.replace(roll, _rolls.get(roll) + 1);
    } else {
      _rolls.put(roll, 1);
    }
  }

  public void usedCheating(){
    _cheatingUsed = true;
  }

}
