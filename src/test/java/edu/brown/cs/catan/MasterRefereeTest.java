package edu.brown.cs.catan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MasterRefereeTest {

  @Test
  public void testConstruction() {
    Referee ref = new MasterReferee();
    assertTrue(ref != null);
    assertFalse(ref.devHasBeenPlayed());
    assertTrue(ref.getTurn() == 1);
  }

  @Test
  public void startNextTurnTest() {
    Referee ref = new MasterReferee();
    assertTrue(ref.getTurn() == 1);
    ref.startNextTurn();
    assertTrue(ref.getTurn() == 2);
  }

  @Test
  public void testGetBoard() {
    Referee ref = new MasterReferee();
    assertTrue(ref.getBoard() != null);
  }

  @Test
  public void testAddPlayers() {
    Referee ref = new MasterReferee();
    ref.addPlayer("John", "#000000");
    ref.addPlayer("Peter", "#0fffff");
    ref.startNextTurn();
    try {
      ref.addPlayer("Not going to work", "color");
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testLookupPlayers() {
    Referee ref = new MasterReferee();
    int id1 = ref.addPlayer("John", "#000000");
    int id2 = ref.addPlayer("Peter", "#0fffff");
    assertTrue(ref.getPlayerByID(id1) != null);
    assertTrue(ref.getPlayerByID(id2) != null);
    assertTrue(ref.getPlayerByID(id1).equals(ref.getPlayerByID(id1)));
    assertFalse(ref.getPlayerByID(id1).equals(ref.getPlayerByID(id2)));
  }

  @Test
  public void testHasLargestArmy() {
    Referee ref = new MasterReferee();
    int id = ref.addPlayer("p1", "color");
    Player player = ref.getPlayerByID(id);
    assertFalse(ref.hasLargestArmy(id));
    player.addDevelopmentCard(DevelopmentCard.KNIGHT);
    player.addDevelopmentCard(DevelopmentCard.KNIGHT);
    player.addDevelopmentCard(DevelopmentCard.KNIGHT);
    assertFalse(ref.hasLargestArmy(id));
    player.playDevelopmentCard(DevelopmentCard.KNIGHT);
    player.playDevelopmentCard(DevelopmentCard.KNIGHT);
    player.playDevelopmentCard(DevelopmentCard.KNIGHT);
    assertTrue(ref.hasLargestArmy(id));

  }

  @Test
  public void testHasLargestArmyComplicated() {
    Referee ref = new MasterReferee();
    int id1 = ref.addPlayer("p1", "color");
    int id2 = ref.addPlayer("p2", "color");
    Player p1 = ref.getPlayerByID(id1);
    Player p2 = ref.getPlayerByID(id2);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    assertTrue(ref.hasLargestArmy(id2));
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    assertTrue(ref.hasLargestArmy(id1));
  }

  @Test
  public void testHasLargestArmyTie() {
    Referee ref = new MasterReferee();
    int id1 = ref.addPlayer("p1", "color");
    int id2 = ref.addPlayer("p2", "color");
    Player p1 = ref.getPlayerByID(id1);
    Player p2 = ref.getPlayerByID(id2);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.addDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p2.playDevelopmentCard(DevelopmentCard.KNIGHT);
    assertTrue(ref.hasLargestArmy(id2));
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    p1.playDevelopmentCard(DevelopmentCard.KNIGHT);
    assertFalse(ref.hasLargestArmy(id1));
    assertTrue(ref.hasLargestArmy(id2));
  }

}
