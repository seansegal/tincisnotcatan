package edu.brown.cs.graph;

import java.util.ArrayList;
import java.util.List;

/** A DummyGraph implemention that can be used to test Graph algorithms. */
class DummyGraph {

  // Hard coded to have 10 vertices, can add any number of edges.
  private List<DummyNode> nodes;
  private static final int NUM_VERTICES = 10;

  /** Creates a DummyGraph. */
  public DummyGraph() {
    nodes = new ArrayList<DummyNode>();
    for (int i = 0; i < NUM_VERTICES; i++) {
      nodes.add(new DummyNode(i));
    }
  }

  /**
   * Adds an edge between two vertices with a given weight.
   *
   * @param v1
   *          Starting Node
   * @param v2
   *          Ending Node
   * @param weight
   *          Edge Weight
   * */
  public void addEdge(int v1, int v2, double weight) {
    if (v1 >= NUM_VERTICES || v2 > NUM_VERTICES || v1 < 0 || v2 < 0) {
      throw new IllegalArgumentException("Invalid vertix ID");
    }
    Edge<Integer, Integer> edge = new Edge<>(nodes.get(v1), nodes.get(v2), 0,
        weight);
    nodes.get(v1).addEdge(edge);
    nodes.get(v2).addEdge(edge);
  }

  /**
   * Returns a Node with a given ID. ID values can range from 0 to 9.
   *
   * @param id
   *          An integer from 0-9 indicating which Node.
   * @return The Node associated with that ID.
   */
  public Node<Integer, Integer> getNodeFromID(int id) {
    return nodes.get(id);
  }
}
