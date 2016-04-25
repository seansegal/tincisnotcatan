package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.HumanPlayer;
import edu.brown.cs.catan.Player;

public class CityTest {

  @Test
  public void InitializationTest() {
    Player p = new HumanPlayer(1, "Name", "000000");
    City c = new City(p);

    assertTrue(c != null);
    assertTrue(c.getPlayer().equals(p));
  }

}
