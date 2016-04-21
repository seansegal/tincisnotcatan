package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntersectionCoordinateTest {

  @Test
  public void testConstruction() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    assertTrue(i != null);
    assertTrue(i.getCoord1().equals(h1));
    assertTrue(i.getCoord2().equals(h2));
    assertTrue(i.getCoord3().equals(h3));
  }

  @Test
  public void testEquals() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i1 = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(m1, m2, m3);

    assertTrue(h1.equals(m1));
    assertTrue(h2.equals(m3));
    assertTrue(h3.equals(m2));
    assertTrue(i1.equals(i2));
  }

  @Test
  public void testHashCode() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i1 = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate i2 = new IntersectionCoordinate(m1, m2, m3);

    assertTrue(i1.hashCode() == i2.hashCode());
    assertTrue(i1.hashCode() == i1.hashCode());

  }
}
