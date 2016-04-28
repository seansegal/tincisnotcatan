package edu.brown.cs.catan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Turn {

  private final long _timeStarted;
  private Map<Integer, Double> _mustDiscard;
  private boolean _devHasBeenPlayed;
  private final int _turnNum;

  public Turn(int turnNum) {
    _timeStarted = System.currentTimeMillis();
    _devHasBeenPlayed = false;
    _mustDiscard = new HashMap<>();
    _turnNum = turnNum;
  }

  public void setDevCardHasBeenPlayed(){
    _devHasBeenPlayed = true;
  }

  public void setMustDiscard(int playerID, double amount){
    _mustDiscard.put(playerID, amount);
  }

  private Turn(Turn turn) {
    _timeStarted = turn.getTimeStarted();
    _devHasBeenPlayed = turn.devHasBeenPlayed();
    _mustDiscard = turn.getMustDiscard();
    _turnNum = turn.getTurnNum();
  }

  public Turn getCopy() {
    return new Turn(this);
  }

  public long getTimeElapsed() {
    return System.currentTimeMillis() - _timeStarted;
  }

  public long getTimeStarted() {
    return _timeStarted;
  }

  public Map<Integer, Double> getMustDiscard() {
    return Collections.unmodifiableMap(_mustDiscard);
  }

  public double getMustDiscard(int playerID) {
    if(_mustDiscard.containsKey(playerID)){
      return _mustDiscard.get(playerID);
    }
    return 0.0;
  }

  public boolean devHasBeenPlayed() {
    return _devHasBeenPlayed;
  }

  public int getTurnNum() {
    return _turnNum;
  }

}
