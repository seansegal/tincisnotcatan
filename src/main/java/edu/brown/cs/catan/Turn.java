package edu.brown.cs.catan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.actions.FollowUpAction;

/**
 * Represents a Standard Turn in Catan and keeps track of all necessary data
 * associated with a Turn. For example, it keeps track of whether the player has
 * used a development card during the turn.
 *
 */
public class Turn {

  private final long _timeStarted;
  private boolean _devHasBeenPlayed;
  private final int _turnNum;
  private List<Collection<FollowUpAction>> _followUps;
  private Map<DevelopmentCard, Integer> _initialDevCardHand;

  /**
   * Creates a Turn.
   *
   * @param turnNum
   *          The turn number
   * @param initialDevCardHand
   *          The player's initial development card hand.
   */
  public Turn(int turnNum, Map<DevelopmentCard, Integer> initialDevCardHand) {
    _timeStarted = System.currentTimeMillis();
    _devHasBeenPlayed = false;
    _turnNum = turnNum;
    _followUps = new ArrayList<>();
    _initialDevCardHand = new HashMap<>(initialDevCardHand);
  }

  /**
   * Creates a copy of a Turn.
   *
   * @param turn
   *          the Turn to copy.
   */
  private Turn(Turn turn) {
    _timeStarted = turn.getTimeStarted();
    _devHasBeenPlayed = turn.devHasBeenPlayed();
    _turnNum = turn.getTurnNum();
    _followUps = turn.getAllFollowUps();
    _initialDevCardHand = new HashMap<>(turn.getInitialDevCards());
  }

  /* Returns all FollowUps. */
  private List<Collection<FollowUpAction>> getAllFollowUps() {
    List<Collection<FollowUpAction>> list = new ArrayList<>();
    for (Collection<FollowUpAction> el : _followUps) {
      list.add(el);
    }
    return list;
  }

  /**
   * Returns whether the Turn is waiting for a FollowUpAction.
   *
   * @return Boolean whether Turn has a FollowUp
   */
  public boolean waitingForFollowUp() {
    return !_followUps.isEmpty();
  }

  private Map<DevelopmentCard, Integer> getInitialDevCards() {
    return Collections.unmodifiableMap(_initialDevCardHand);
  }

  void addFollowUp(Collection<FollowUpAction> actions) {
    List<FollowUpAction> actionsCopy = new ArrayList<>();
    for (FollowUpAction action : actions) {
      actionsCopy.add(action);
    }
    _followUps.add(actionsCopy);
  }

  /**
   * Returns the next FollowUpAction for a given player.
   *
   * @param playerID
   *          The players id
   * @return The Action that must be performed.
   */
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

  /**
   * Removes a FollowUp.
   *
   * @param action
   *          The FollowUpAction to remove.
   */
  public void removeFollowUp(FollowUpAction action) {
    Collection<FollowUpAction> curr = _followUps.get(0);
    curr.remove(action);
    if (curr.isEmpty()) {
      _followUps.remove(curr);
    }
  }

  /**
   * Should be called when a development card is played in a turn.
   */
  public void setDevCardHasBeenPlayed() {
    _devHasBeenPlayed = true;
  }

  /**
   * Returns a copy of the Turn.
   *
   * @return A Turn.
   */
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

  /**
   * Returns whether the current player had the given development card before
   * the turn started.
   *
   * @param dev
   *          The DevelopmentCard
   * @return Whether the player had the development card.
   */
  public boolean hadInitialDevCard(DevelopmentCard dev) {
    return _initialDevCardHand.get(dev) > 0;
  }

}
