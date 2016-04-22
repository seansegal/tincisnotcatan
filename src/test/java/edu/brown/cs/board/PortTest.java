package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.catan.Resource;

public class PortTest {

  @Test
  public void InitializationTest() {
    Port p = new Port(Resource.WHEAT);

    assertTrue(p != null);
    assertTrue(p.getResource() == Resource.WHEAT);
  }

}
