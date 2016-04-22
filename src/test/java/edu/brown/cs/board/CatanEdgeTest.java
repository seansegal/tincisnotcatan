package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.graph.Node;

public class CatanEdgeTest {

  @Test
  public void InitializationTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate startIC = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate h4 = new HexCoordinate(0, 0, 0);
    HexCoordinate h5 = new HexCoordinate(1, 0, 0);
    HexCoordinate h6 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate endIC = new IntersectionCoordinate(h4, h5, h6);
    Intersection start = new Intersection(startIC);
    Intersection end = new Intersection(endIC);

    Path p = new Path(start, end);
    Node<Path, Intersection> cn = new CatanNode(start);
    Node<Path, Intersection> cn2 = new CatanNode(end);

    CatanEdge ce = new CatanEdge(cn, cn2, p, 1.0);

    assertTrue(ce != null);
    assertTrue(ce.getData().equals(p));
    assertTrue(ce.getNode1().equals(cn));
    assertTrue(ce.getNode2().equals(cn2));
    assertTrue(ce.getWeight() == 1.0);

  }

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

  }

  @Test
  public void GetAdjacentTest() {

  }

  @Test
  public void HashTest() {

  }

}
