package edu.brown.cs.catan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.brown.cs.actions.FollowUpAction;

public class Turn {

  private final long _timeStarted;
  private boolean _devHasBeenPlayed;
  private final int _turnNum;
  private List<Collection<FollowUpAction>> _followUps;

  public Turn(int turnNum) {
    _timeStarted = System.currentTimeMillis();
    _devHasBeenPlayed = false;
    _turnNum = turnNum;
    _followUps = new ArrayList<>();
  }

  private Turn(Turn turn) {
    _timeStarted = turn.getTimeStarted();
    _devHasBeenPlayed = turn.devHasBeenPlayed();
    _turnNum = turn.getTurnNum();
    _followUps = turn.getAllFollowUps();
  }

  private List<Collection<FollowUpAction>> getAllFollowUps() {
    List<Collection<FollowUpAction>> list = new ArrayList<>();
    for (Collection<FollowUpAction> el : _followUps) {
      list.add(el);
    }
    return list;
  }

  public boolean waitingForFollowUp() {
    return !_followUps.isEmpty();
  }

  void addFollowUp(Collection<FollowUpAction> actions) {
    List<FollowUpAction> actionsCopy = new ArrayList<>();
    for (FollowUpAction action : actions) {
      actionsCopy.add(action);
    }
    _followUps.add(actionsCopy);
  }

  public FollowUpAction getNextFollowUp(int playerID) {
    if (!_followUps.isEmpty()) {
      Collection<FollowUpAction> curr = _followUps.get(0);
      for (FollowUpAction action : curr) {
        if (action.getPlayerID() == playerID) {
          return action;
        }
      }
    }
    return null;
  }

  public void removeFollowUp(FollowUpAction action) {
    Collection<FollowUpAction> curr = _followUps.get(0);
    curr.remove(action);
    if (curr.isEmpty()) {
      _followUps.remove(curr);
    }
  }

  public void setDevCardHasBeenPlayed() {
    _devHasBeenPlayed = true;
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

  public boolean devHasBeenPlayed() {
    return _devHasBeenPlayed;
  }

  public int getTurnNum() {
    return _turnNum;
  }

}
