package edu.brown.cs.catan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Turn {

  private final long _timeStarted;
  private Map<Integer, Double> _mustDiscard;
  private boolean _devHasBeenPlayed;
  private final int _turnNum;
  private List<Map<Integer, String>> _followUps;

  public Turn(int turnNum) {
    _timeStarted = System.currentTimeMillis();
    _devHasBeenPlayed = false;
    _mustDiscard = new HashMap<>();
    _turnNum = turnNum;
    _followUps = new ArrayList<>();
  }

  private Turn(Turn turn) {
    _timeStarted = turn.getTimeStarted();
    _devHasBeenPlayed = turn.devHasBeenPlayed();
    _mustDiscard = turn.getMustDiscard();
    _turnNum = turn.getTurnNum();
    _followUps = turn.getAllFollowUps();
  }

  private List<Map<Integer, String>> getAllFollowUps() {
    List<Map<Integer, String>> list = new ArrayList<>();
    for (Map<Integer, String> el : _followUps) {
      list.add(el);
    }
    return list;
  }

  void addFollowUp(Map<Integer, String> actions) {
    _followUps.add(actions);
  }

  public Map<Integer, String> getNextFollowUp() {
    if (_followUps.isEmpty()) {
      return Collections.emptyMap();
    }
    return Collections.unmodifiableMap(_followUps.get(0));
  }

  void removeFollowUp(int playerID, String action) {
    Map<Integer, String> newFollowUps = new HashMap<>();
    Map<Integer, String> currentFollowUps = _followUps.remove(0);
    for (Map.Entry<Integer, String> followUp : currentFollowUps.entrySet()) {
      if (playerID != followUp.getKey() || action != followUp.getValue()) {
        newFollowUps.put(followUp.getKey(), followUp.getValue());
      }
    }
    if(!newFollowUps.isEmpty()){
      _followUps.add(0, newFollowUps);
    }
  }

  public void setDevCardHasBeenPlayed() {
    _devHasBeenPlayed = true;
  }

  public void setMustDiscard(int playerID, double amount) {
    _mustDiscard.put(playerID, amount);
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
    if (_mustDiscard.containsKey(playerID)) {
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
