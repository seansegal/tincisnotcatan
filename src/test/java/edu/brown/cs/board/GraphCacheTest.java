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
    GraphCache cache = new GraphCache();
    CatanNode cn = new CatanNode(inter, cache);
    cache.addNode(inter, cn);
    assertTrue(cache.getNode(inter) != null);
    assertTrue(cache.getNode(inter).equals(cn));
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
    GraphCache cache = new GraphCache();
    Path p = new Path(start, end);
    Node<Path, Intersection> cn = new CatanNode(start, cache);
    Node<Path, Intersection> cn2 = new CatanNode(end, cache);

    CatanEdge ce = new CatanEdge(cn, cn2, p, 1.0);
    cache.addEdge(p, ce);
    assertTrue(cache.getEdge(p) != null);
    assertTrue(cache.getEdge(p).equals(ce));
  }

}
