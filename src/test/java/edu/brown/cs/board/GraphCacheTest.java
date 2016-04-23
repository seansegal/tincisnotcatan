package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.graph.Node;

public class GraphCacheTest {

  @Test
  public void NodeCacheTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);

    CatanNode cn = new CatanNode(inter);
    assertTrue(GraphCache.nodes.get(inter) == null);
    GraphCache.nodes.put(inter, cn);
    assertTrue(GraphCache.nodes.containsKey(inter));
    assertTrue(GraphCache.nodes.get(inter).equals(cn));
  }

  @Test
  public void EdgeCacheTest() {
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
    assertTrue(GraphCache.edges.get(p) == null);
    GraphCache.edges.put(p, ce);
    assertTrue(GraphCache.edges.containsKey(p));
    assertTrue(GraphCache.edges.get(p).equals(ce));
  }

}
