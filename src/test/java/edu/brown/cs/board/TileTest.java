package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TileTest {

  @Test
  public void InitializationTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();

    Tile t = new Tile(1, h1, intersections, paths, TileType.WHEAT);
    Tile t2 = new Tile(2, h1, intersections, paths, TileType.WHEAT, true);
    assertTrue(t != null);
    assertTrue(t.getRollNumber() == 1);
    assertTrue(t.getCoordinate().equals(h1));
    assertTrue(t.getIntersections().size() == 6);
    assertTrue(t.hasRobber() == false);
  }

  @Test
  public void RobberTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();

    Tile t = new Tile(1, h1, intersections, paths, TileType.WHEAT);
    Tile t2 = new Tile(2, h1, intersections, paths, TileType.WHEAT, true);
    assertTrue(t.hasRobber() == false);
    assertTrue(t2.hasRobber() == true);
    t2.hasRobber(false);
    t.hasRobber(true);
    assertTrue(t.hasRobber() == true);
    assertTrue(t2.hasRobber() == false);
  }

  @Test
  public void EqualityTest() {
    HexCoordinate h1 = new HexCoordinate(1, 1, 1);
    HexCoordinate h2 = new HexCoordinate(0, 0, 0);
    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();

    Tile t = new Tile(1, h1, intersections, paths, TileType.WHEAT);
    Tile t2 = new Tile(2, h2, intersections, paths, TileType.SHEEP);

    assertTrue(t.equals(t2));
  }

  @Test
  public void HashTest() {
    HexCoordinate h1 = new HexCoordinate(1, 1, 1);
    HexCoordinate h2 = new HexCoordinate(0, 0, 0);
    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();

    Tile t = new Tile(1, h1, intersections, paths, TileType.WHEAT);
    Tile t2 = new Tile(2, h2, intersections, paths, TileType.SHEEP);

    assertTrue(t.hashCode() == t2.hashCode());
    assertTrue(t.hashCode() == t.hashCode());
  }

}
