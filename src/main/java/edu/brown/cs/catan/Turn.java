package edu.brown.cs.catan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.actions.FollowUpAction;

public class Turn {

  private final long _timeStarted;
  private boolean _devHasBeenPlayed;
  private final int _turnNum;
  private List<Collection<FollowUpAction>> _followUps;
  private Map<DevelopmentCard, Integer> _initialDevCardHand;

  public Turn(int turnNum, Map<DevelopmentCard, Integer> initialDevCardHand) {
    _timeStarted = System.currentTimeMillis();
    _devHasBeenPlayed = false;
    _turnNum = turnNum;
    _followUps = new ArrayList<>();
    _initialDevCardHand = new HashMap<>(initialDevCardHand);
  }

  private Turn(Turn turn) {
    _timeStarted = turn.getTimeStarted();
    _devHasBeenPlayed = turn.devHasBeenPlayed();
    _turnNum = turn.getTurnNum();
    _followUps = turn.getAllFollowUps();
    _initialDevCardHand = new HashMap<>(turn.getInitialDevCards());
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

  private  Map<DevelopmentCard, Integer> getInitialDevCards(){
    return Collections.unmodifiableMap(_initialDevCardHand);
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

  public boolean hadInitialDevCard(DevelopmentCard dev) {
    return _initialDevCardHand.get(dev) > 0;
  }

}
