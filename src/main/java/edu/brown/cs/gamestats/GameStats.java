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

  public int[] getRollsArray() {
    int[] rolls = new int[11];
    rolls[0] = _rolls.containsKey(2) ? _rolls.get(2) : 0;
    rolls[1] = _rolls.containsKey(3) ? _rolls.get(3) : 0;
    rolls[2] = _rolls.containsKey(4) ? _rolls.get(4) : 0;
    rolls[3] = _rolls.containsKey(5) ? _rolls.get(5) : 0;
    rolls[4] = _rolls.containsKey(6) ? _rolls.get(6) : 0;
    rolls[5] = _rolls.containsKey(7) ? _rolls.get(7) : 0;
    rolls[6] = _rolls.containsKey(8) ? _rolls.get(8) : 0;
    rolls[7] = _rolls.containsKey(9) ? _rolls.get(9) : 0;
    rolls[8] = _rolls.containsKey(10) ? _rolls.get(10) : 0;
    rolls[9] = _rolls.containsKey(11) ? _rolls.get(11) : 0;
    rolls[10] = _rolls.containsKey(12) ? _rolls.get(12) : 0;
    return rolls;
  }

  public void usedCheating() {
    _cheatingUsed = true;
  }

}
