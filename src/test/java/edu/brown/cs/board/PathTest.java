package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PathTest {

  @Test
  public void InitializationTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate start = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate end = new IntersectionCoordinate(h4, h5, h6);

    Path p = new Path(new Intersection(start), new Intersection(end));

    assertTrue(p != null);
    assertTrue(p.getStart().equals(new Intersection(start)));
    assertTrue(p.getEnd().equals(new Intersection(end)));
  }

  // @Test
  // public void RoadStartBuildingTest() {
  // HexCoordinate h1 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h2 = new HexCoordinate(0, 1, 0);
  // HexCoordinate h3 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate startIC = new IntersectionCoordinate(h1, h2, h3);
  // Intersection start = new Intersection(startIC);
  //
  // HexCoordinate h4 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h5 = new HexCoordinate(1, 0, 0);
  // HexCoordinate h6 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate endIC = new IntersectionCoordinate(h4, h5, h6);
  // Intersection end = new Intersection(endIC);
  //
  // Path p = new Path(start, end);
  // Player hp = new HumanPlayer(1, "1", "000000");
  // assertFalse(p.canPlaceRoad(hp));
  // start.placeSettlement(hp);
  // assertTrue(p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() == null);
  // p.placeRoad(hp);
  // assertTrue(!p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() != null);
  // }
  //
  // @Test
  // public void RoadEndBuildingTest() {
  // HexCoordinate h1 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h2 = new HexCoordinate(0, 1, 0);
  // HexCoordinate h3 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate startIC = new IntersectionCoordinate(h1, h2, h3);
  // Intersection start = new Intersection(startIC);
  //
  // HexCoordinate h4 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h5 = new HexCoordinate(1, 0, 0);
  // HexCoordinate h6 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate endIC = new IntersectionCoordinate(h4, h5, h6);
  // Intersection end = new Intersection(endIC);
  //
  // Path p = new Path(start, end);
  // Player hp = new HumanPlayer(1, "1", "000000");
  // assertFalse(p.canPlaceRoad(hp));
  // end.placeSettlement(hp);
  // assertTrue(p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() == null);
  // p.placeRoad(hp);
  // assertTrue(!p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() != null);
  // }
  //
  // @Test
  // public void RoadRoadBuildingTest() {
  // HexCoordinate h1 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h2 = new HexCoordinate(0, 1, 0);
  // HexCoordinate h3 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate startIC = new IntersectionCoordinate(h1, h2, h3);
  // Intersection start = new Intersection(startIC);
  //
  // HexCoordinate h4 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h5 = new HexCoordinate(1, 0, 0);
  // HexCoordinate h6 = new HexCoordinate(0, 0, -1);
  // IntersectionCoordinate endIC = new IntersectionCoordinate(h4, h5, h6);
  // Intersection end = new Intersection(endIC);
  //
  // HexCoordinate h7 = new HexCoordinate(0, 0, 0);
  // HexCoordinate h8 = new HexCoordinate(1, 1, 0);
  // HexCoordinate h9 = new HexCoordinate(2, 1, 0);
  // IntersectionCoordinate thirdIC = new IntersectionCoordinate(h7, h8, h9);
  // Intersection third = new Intersection(thirdIC);
  //
  // Path p = new Path(start, end);
  // Path p2 = new Path(end, third);
  // Player hp = new HumanPlayer(1, "1", "000000");
  // assertFalse(p.canPlaceRoad(hp));
  // start.placeSettlement(hp);
  // assertTrue(p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() == null);
  // p.placeRoad(hp);
  // assertTrue(!p.canPlaceRoad(hp));
  // assertTrue(p.getRoad() != null);
  // assertTrue(p2.canPlaceRoad(hp));
  // }

  @Test
  public void EqualityTest() {
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

    Path p = new Path(new Intersection(start), new Intersection(end));
    Path p2 = new Path(new Intersection(start2), new Intersection(end2));

    assertTrue(h1.equals(m1));
    assertTrue(h2.equals(m3));
    assertTrue(h3.equals(m2));
    assertTrue(h4.equals(m4));
    assertTrue(h5.equals(m6));
    assertTrue(h6.equals(m5));
    assertTrue(p.equals(p2));
  }

  @Test
  public void HashTest() {
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

    Path p = new Path(new Intersection(start), new Intersection(end));
    Path p2 = new Path(new Intersection(start2), new Intersection(end2));

    assertTrue(p.hashCode() == p2.hashCode());
    assertTrue(p.hashCode() == p.hashCode());
  }

}
