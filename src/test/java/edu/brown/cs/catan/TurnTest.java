package edu.brown.cs.catan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.actions.FollowUpAction;

public class TurnTest {

  @Test
  public void testConstruction() {
    Turn turn = new Turn(1);
    assertTrue(turn != null);
    assertTrue(turn.getTurnNum() == 1);
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
  public void testBasicFollowUp(){
    Turn turn = new Turn(1);
    assertFalse(turn.waitingForFollowUp());
    Collection<FollowUpAction> followUp = ImmutableList.of(new TestFollowUp(0));
    turn.addFollowUp(followUp);
    assertTrue(turn.waitingForFollowUp());
  }

  private static class TestFollowUp implements FollowUpAction{

    private int player;

    public TestFollowUp(int player){
      this.player = player;
    }

    @Override
    public Map<Integer, ActionResponse> execute() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public JsonObject getData() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getID() {
      return "testAction";
    }

    @Override
    public int getPlayerID() {
      return this.player;
    }

    @Override
    public void setupAction(Referee ref, int playerID, JsonObject params) {
      // TODO Auto-generated method stub
    }

  }

}


