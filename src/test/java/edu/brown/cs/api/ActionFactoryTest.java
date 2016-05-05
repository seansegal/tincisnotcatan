package edu.brown.cs.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;

public class ActionFactoryTest {

  @Test
  public void testCreateNonexistantAction() {
    Referee ref = new MasterReferee();
    ActionFactory factory = new ActionFactory(ref);
    try {
      String json = "{action: doesntExist, player: 0}";
      factory.createAction(json);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (WaitingOnActionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testBadJson() {
    Referee ref = new MasterReferee();
    ActionFactory factory = new ActionFactory(ref);
    try {
      String json = "{action: buildSettlement, player: 0";
      factory.createAction(json);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (WaitingOnActionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  public void testMissingPlayerID() {
    Referee ref = new MasterReferee();
    ActionFactory factory = new ActionFactory(ref);
    try {
      String json = "{action: rollDice}";

      factory.createAction(json);

      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (WaitingOnActionException e) {
      e.printStackTrace();
      assertTrue(false);
    }
  }
}
