package edu.brown.cs.catan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.actions.DropCards;

public class TurnTest {

  @Test
  public void testConstruction() {
    Turn turn = new Turn(1);
    assertTrue(turn != null);
    assertTrue(turn.getTurnNum() == 1);
  }

  @Test
  public void testMustDiscard() {
    Turn turn = new Turn(1);
    assertTrue(turn.getMustDiscard(0) == 0.0);
    turn.setMustDiscard(0, 5.4);
    assertTrue(turn.getMustDiscard(0) == 5.4);
    turn.setMustDiscard(1, 4.3);
    assertTrue(turn.getMustDiscard().get(0) == 5.4);
    assertTrue(turn.getMustDiscard().get(1) == 4.3);
  }

  @Test
  public void testDevHasBeenPlayed() {
    Turn turn = new Turn(2);
    assertFalse(turn.devHasBeenPlayed());
    turn.setDevCardHasBeenPlayed();
    assertTrue(turn.devHasBeenPlayed());
  }

  @Test
  public void testTimeElasped() {
    Turn turn = new Turn(1);
    try {
      TimeUnit.MILLISECONDS.sleep(10);
      assertTrue(turn.getTimeElapsed() >= 10);
    } catch (InterruptedException e) {
      assertTrue(false);
    }
  }

  @Test
  public void followUpTest(){
    Turn t = new Turn(1);
    t.addFollowUp(ImmutableMap.of(0, DropCards.ID));
    assertTrue(t.getNextFollowUp().containsKey(0));
    assertTrue(t.getCopy().getNextFollowUp().containsKey(0));
  }

  @Test
  public void addMultipleFollowUpsTest(){
    Turn t = new Turn(1);
    Map<Integer, String> followUps = ImmutableMap.of(1, "doOne", 2, "doTwo", 3, "doThree");
    t.addFollowUp(followUps);
    assertTrue(t.getCopy().getNextFollowUp().equals(followUps));
    Map<Integer, String> followUpsAfter = ImmutableMap.of(1, "doOneMore", 4, "doFour");
    t.addFollowUp(followUpsAfter);
    assertTrue(t.getCopy().getNextFollowUp().equals(followUps));
  }

  @Test
  public void removeFollowUpTests(){
    Turn t = new Turn(1);
    Map<Integer, String> followUps = ImmutableMap.of(1, "doOne", 2, "doTwo", 3, "doThree");
    t.addFollowUp(followUps);
    assertTrue(t.getCopy().getNextFollowUp().equals(followUps));
    Map<Integer, String> followUpsAfter = ImmutableMap.of(1, "doOneMore", 4, "doFour");
    t.addFollowUp(followUpsAfter);
    assertTrue(t.getCopy().getNextFollowUp().equals(followUps));
    t.removeFollowUp(1, "doOne");
    assertTrue(t.getNextFollowUp().containsKey(2));
    t.removeFollowUp(2, "doTwo");
    assertTrue(t.getNextFollowUp().containsKey(3));
    assertFalse(t.getNextFollowUp().containsKey(4));
    t.removeFollowUp(3, "doThree");
    assertTrue(t.getNextFollowUp().containsKey(1));
    assertTrue(t.getNextFollowUp().containsKey(4));
    assertTrue(t.getCopy().getNextFollowUp().equals(followUpsAfter));
  }
}
