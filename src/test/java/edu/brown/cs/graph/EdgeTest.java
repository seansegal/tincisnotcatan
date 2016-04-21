package edu.brown.cs.graph;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EdgeTest {

  @Test
  public void testEdgeConstructor() {
    Edge<Integer, Integer> edge = new Edge<Integer, Integer>(new DummyNode(1),
        new DummyNode(1), 1, 2.2);
    assertTrue(edge != null);
    assertTrue(edge.getWeight() == 2.2);
    assertTrue(edge.getData() == 1);
    assertTrue(edge.getNode1().equals(new DummyNode(1)));
    assertTrue(edge.getNode2().equals(new DummyNode(1)));
  }

  @Test
  public void testInvalidConstruction() {
    try {
      new Edge<Integer, Integer>(null, null, 1, 2.2);
      assertTrue(false);
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
  }

  @Test
  public void testInvalidAdjacent(){
    Edge<Integer, Integer> edge = new Edge<Integer, Integer>(new DummyNode(1),
        new DummyNode(1), 1, 2.2);
    try{
      edge.getAdjacent(new DummyNode(22));
    }
    catch(IllegalArgumentException e){
      assertTrue(true);
    }

  }

}
