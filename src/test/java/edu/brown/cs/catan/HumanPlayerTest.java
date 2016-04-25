package edu.brown.cs.catan;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class HumanPlayerTest {

  @Test
  public void testConstruction() {
    Player player = new HumanPlayer(0, "", "");
    assertTrue(player != null);
    assertTrue(player.numRoads() == Settings.INITIAL_ROADS);
    assertTrue(player.numCities() == Settings.INITIAL_CITIES);
    assertTrue(player.numSettlements() == Settings.INITIAL_SETTLEMENTS);
  }

  // These build test assume normal costs for buildings:
  @Test
  public void testBuildRoad() {
    Player player = new HumanPlayer(0, "", "");
    player.addResource(Resource.BRICK);
    player.addResource(Resource.WOOD);
    player.buildRoad();
    assertTrue(playerOutOfResources(player));
  }

  @Test
  public void testBuildSettlement() {
    Player player = new HumanPlayer(0, "", "");
    player.addResource(Resource.BRICK);
    player.addResource(Resource.WHEAT);
    player.addResource(Resource.SHEEP);
    player.addResource(Resource.WOOD);
    player.buildSettlement();
    assertTrue(playerOutOfResources(player));
  }

  @Test
  public void testBuildCity() {
    Player player = new HumanPlayer(0, "", "");
    // Must first use a settlement
    player.useSettlement();
    player.addResource(Resource.ORE);
    player.addResource(Resource.ORE);
    player.addResource(Resource.ORE);
    player.addResource(Resource.WHEAT);
    player.addResource(Resource.WHEAT);
    player.buildCity();
    assertTrue(playerOutOfResources(player));
  }

  @Test
  public void testBuyDevelopment() {
    Player player = new HumanPlayer(0, "", "");
    player.addResource(Resource.WHEAT);
    player.addResource(Resource.SHEEP);
    player.addResource(Resource.ORE);
    player.buyDevelopmentCard();
    assertTrue(playerOutOfResources(player));
  }

  private boolean playerOutOfResources(Player player) {
    for (Map.Entry<Resource, Double> hand : player.getResources().entrySet()) {
      if (hand.getValue() != Settings.INITIAL_RESOURCES) {
        return false;
      }
    }
    return true;
  }

  // Development Card Tests:
  @Test
  public void testDevelopmentCard() {
    Player player = new HumanPlayer(0, "", "");
    for (DevelopmentCard dev : DevelopmentCard.values()) {
      player.addDevelopmentCard(dev);
      assertTrue(player.getDevCards().get(dev) == 1);
      player.playDevelopmentCard(dev);
      assertTrue(player.getDevCards().get(dev) == 0);
      player.addDevelopmentCard(dev);
      player.addDevelopmentCard(dev);
      assertTrue(player.getDevCards().get(dev) == 2);
    }
  }

  // Test hashCode and equals:
  @Test
  public void testHashCode() {
    Player p1 = new HumanPlayer(0, "", "");
    Player p2 = new HumanPlayer(1, "", "");
    assertTrue(p1.hashCode() == p1.hashCode());
    assertTrue(p2.hashCode() == p2.hashCode());
    assertTrue(p1.hashCode() != p2.hashCode());
  }

  @Test
  public void testEquals() {
    Player p1 = new HumanPlayer(2, "", "");
    Player p2 = new HumanPlayer(3, "", "");
    assertTrue(p1.equals(p1));
    assertTrue(p2.equals(p2));
    assertTrue(!p1.equals(p2));
  }

  // ImmutablePlayer Tests:
  @Test
  public void testImmutablePlayer() {
    Player player = new HumanPlayer(2, "", "");
    player.addResource(Resource.BRICK);
    player.addResource(Resource.WHEAT);
    player.addResource(Resource.SHEEP);
    player.addResource(Resource.SHEEP);
    player.addResource(Resource.SHEEP);

    // Game start
    player.useSettlement();
    player.useRoad();
    player.useSettlement();
    player.useRoad();

    player.addDevelopmentCard(DevelopmentCard.KNIGHT);

    Player immutable = player.getImmutableCopy();

    // Valid operations:
    assertTrue(immutable.numCities() == Settings.INITIAL_CITIES);
    assertTrue(immutable.numRoads() == Settings.INITIAL_ROADS - 2);
    assertTrue(immutable.numSettlements() == Settings.INITIAL_SETTLEMENTS - 2);
    assertTrue(immutable.getDevCards().get(DevelopmentCard.KNIGHT) == 1);
    assertTrue(immutable.getNumResourceCards() == 5.0);

    // Invalid Operations:
    try {
      immutable.addResource(Resource.WHEAT);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.addDevelopmentCard(DevelopmentCard.KNIGHT);
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.buildCity();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.buildSettlement();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.buildRoad();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.useCity();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.useRoad();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
    try {
      immutable.useSettlement();
    } catch (UnsupportedOperationException e) {
      assertTrue(true);
    }
  }
}
