package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.HumanPlayer;
import edu.brown.cs.catan.Player;

public class SettlementTest {

  @Test
  public void InitializationTest() {
    Player p = new HumanPlayer(1, "name", "000000");
    Settlement s = new Settlement(p);

    assertTrue(s != null);
    assertTrue(s.getPlayer().equals(p));
  }

}
