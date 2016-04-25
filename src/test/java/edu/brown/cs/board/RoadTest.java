package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.HumanPlayer;
import edu.brown.cs.catan.Player;

public class RoadTest {

  @Test
  public void InitializationTest() {
    Player p = new HumanPlayer(1, "name", "000000");
    Road r = new Road(p);

    assertTrue(r != null);
    assertTrue(r.getPlayer().equals(p));
  }

}
