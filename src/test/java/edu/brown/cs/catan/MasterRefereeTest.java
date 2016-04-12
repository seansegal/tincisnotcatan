package edu.brown.cs.catan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MasterRefereeTest {

  @Test
  public void testConstruction(){
    Referee ref = new MasterReferee(4, false);
    assertTrue(ref != null);
    assertFalse(ref.devHasBeenPlayed());
    assertTrue(ref.getTurn() == 1);
  }

  @Test
  public void startNextTurnTest(){
    Referee ref = new MasterReferee();
    assertTrue(ref.getTurn() == 1);
    ref.startNextTurn();
    assertTrue(ref.getTurn() == 2);
  }



}
