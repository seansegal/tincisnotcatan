package edu.brown.cs.board;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.graph.Node;

public class CatanNodeTest {

  @Test
  public void InitializationTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate i = new IntersectionCoordinate(h1, h2, h3);
    Intersection inter = new Intersection(i);
    GraphCache cache = new GraphCache();
    CatanNode cn = new CatanNode(inter, cache);
    assertTrue(cn != null);
    assertTrue(cn.getData().equals(inter));
  }

  @Test
  public void GetEdgesTest() {
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

    assertTrue(cn.getEdges() != null);
    assertTrue(cn.getEdges().size() == 1);
    assertTrue(cn.getEdges().contains(new CatanEdge(cn, cn2, p, 1.0)));
    assertTrue(cn2.getEdges() != null);
    assertTrue(cn2.getEdges().size() == 1);
    assertTrue(cn2.getEdges().contains(new CatanEdge(cn, cn2, p, 1.0)));
  }

  @Test
  public void EqualityTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate ic1 = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate ic2 = new IntersectionCoordinate(m1, m2, m3);
    Intersection inter = new Intersection(ic1);
    Intersection inter2 = new Intersection(ic2);
    GraphCache cache = new GraphCache();
    Node<Path, Intersection> cn = new CatanNode(inter, cache);
    Node<Path, Intersection> cn2 = new CatanNode(inter2, cache);

    assertTrue(cn.equals(cn2));

  }

  @Test
  public void HashTest() {
    HexCoordinate h1 = new HexCoordinate(0, 0, 0);
    HexCoordinate h2 = new HexCoordinate(0, 1, 0);
    HexCoordinate h3 = new HexCoordinate(0, 0, -1);
    IntersectionCoordinate ic1 = new IntersectionCoordinate(h1, h2, h3);

    HexCoordinate m1 = new HexCoordinate(1, 1, 1);
    HexCoordinate m2 = new HexCoordinate(2, 2, 1);
    HexCoordinate m3 = new HexCoordinate(-1, 0, -1);
    IntersectionCoordinate ic2 = new IntersectionCoordinate(m1, m2, m3);
    Intersection inter = new Intersection(ic1);
    Intersection inter2 = new Intersection(ic2);
    GraphCache cache = new GraphCache();
    Node<Path, Intersection> cn = new CatanNode(inter, cache);
    Node<Path, Intersection> cn2 = new CatanNode(inter2, cache);

    assertTrue(cn.hashCode() == cn2.hashCode());
    assertTrue(cn.hashCode() == cn.hashCode());
  }

}
