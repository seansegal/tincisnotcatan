package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PathCoordinateTest {

  @Test
  public void testConstruction() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate start = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate end = new IntersectionCoordinate(h4, h5, h6);

    PathCoordinate p = new PathCoordinate(start, end);

    assertTrue(p != null);
    assertTrue(p.get_startCoord().equals(start));
    assertTrue(p.get_endCoord().equals(end));
  }

  @Test
  public void testEquals() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate start = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate start2 = new IntersectionCoordinate(m1, m2, m3);

    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate end = new IntersectionCoordinate(h4, h5, h6);

    HexCoordinate m4 = new HexCoordinate(1, 1, 1);
    HexCoordinate m5 = new HexCoordinate(2, 2, 1);
    HexCoordinate m6 = new HexCoordinate(0, -1, -1);
    IntersectionCoordinate end2 = new IntersectionCoordinate(m4, m5, m6);

    PathCoordinate p = new PathCoordinate(start, end);
    PathCoordinate p2 = new PathCoordinate(start2, end2);

    assertTrue(h1.equals(m1));
    assertTrue(h2.equals(m3));
    assertTrue(h3.equals(m2));
    assertTrue(h4.equals(m4));
    assertTrue(h5.equals(m6));
    assertTrue(h6.equals(m5));
    assertTrue(p.equals(p2));
  }

  @Test
  public void testHashCode() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate start = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate start2 = new IntersectionCoordinate(m1, m2, m3);

    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate end = new IntersectionCoordinate(h4, h5, h6);

    HexCoordinate m4 = new HexCoordinate(1, 1, 1);
    HexCoordinate m5 = new HexCoordinate(2, 2, 1);
    HexCoordinate m6 = new HexCoordinate(0, -1, -1);
    IntersectionCoordinate end2 = new IntersectionCoordinate(m4, m5, m6);

    PathCoordinate p = new PathCoordinate(start, end);
    PathCoordinate p2 = new PathCoordinate(start2, end2);

    assertTrue(p.hashCode() == p2.hashCode());
    assertTrue(p.hashCode() == p.hashCode());

  }

}
