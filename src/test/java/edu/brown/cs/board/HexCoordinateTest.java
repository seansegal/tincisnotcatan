package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HexCoordinateTest {

  @Test
  public void testConstruction() {
    HexCoordinate cord = new HexCoordinate(1, 0, 2);
    assertTrue(cord != null);
    assertTrue(cord.getX() == 1);
    assertTrue(cord.getY() == 0);
    assertTrue(cord.getZ() == 2);
  }

  @Test
  public void testConversionToCartesianX() {
    HexCoordinate cord1 = new HexCoordinate(1, 0, 1);
    HexCoordinate cord2 = new HexCoordinate(0, 1, 0);
    assertTrue(cord1.cartesianX() == -1);
    assertTrue(cord2.cartesianX() == 1);
  }

  @Test
  public void testConversionToCartesianY() {
    HexCoordinate cord1 = new HexCoordinate(1, 2, 4);
    assertTrue(cord1.cartesianX() < 2.5981);
    assertTrue(cord1.cartesianY() > 2.5979);
  }

  @Test
  public void testEquals() {
    HexCoordinate cord1 = new HexCoordinate(2, 1, 2);
    HexCoordinate cord2 = new HexCoordinate(1, 0, 1);
    assertTrue(cord1.equals(cord1));
    assertTrue(cord2.equals(cord2));
    assertTrue(cord1.equals(cord2));

    HexCoordinate p1 = new HexCoordinate(1, 0, 0);
    HexCoordinate p2 = new HexCoordinate(0, -1, -1);
    assertTrue(p1.equals(p1));
    assertTrue(p2.equals(p2));
    assertTrue(p1.equals(p2));

    HexCoordinate p3 = new HexCoordinate(2, 1, 3);
    HexCoordinate p4 = new HexCoordinate(1, 0, 2);
    assertTrue(p4.equals(p3));
    assertTrue(p3.equals(p4));
  }

  @Test
  public void testHashCode() {
    HexCoordinate cord1 = new HexCoordinate(2, 1, 2);
    HexCoordinate cord2 = new HexCoordinate(1, 0, 1);
    assertTrue(cord1.hashCode() == cord1.hashCode());
    assertTrue(cord1.hashCode() == cord2.hashCode());

    HexCoordinate p1 = new HexCoordinate(1, 0, 0);
    HexCoordinate p2 = new HexCoordinate(0, -1, -1);
    assertTrue(p1.hashCode() == p1.hashCode());
    assertTrue(p1.hashCode() == p2.hashCode());
  }

}
