package edu.brown.cs.graph;

import java.util.ArrayList;
import java.util.List;

/** A Dummy Node used in the implementation of DummyGraph. */
class DummyNode implements Node<Integer, Integer> {

  // Stores it's data and the edges it is connected to:
  private int data;
  private List<Edge<Integer, Integer>> edges;

  /**
   * Creates an instance of a DummyNode.
   *
   * @param data
   *          An integer data value to hold.
   */
  DummyNode(int data) {
    this.data = data;
    this.edges = new ArrayList<Edge<Integer, Integer>>();
  }

  /**
   * Adds an edge to the Node.
   *
   * @param edge
   *          The edge to be added.
   */
  void addEdge(Edge<Integer, Integer> edge) {
    edges.add(edge);
  }

  /**
   * Returns a list of the Nodes edges.
   *
   * @return A list of Edge<Integer, Integer> corresponding to the Node's edges
   */
  @Override
  public List<Edge<Integer, Integer>> getEdges() {
    return edges;
  }

  /**
   * Returns the integer data associated with the DummyNode.
   *
   * @return An integer as data.
   */
  @Override
  public Integer getData() {
    return data;
  }

  /**
   * Creates a String representation of the Node.
   *
   * @return A String representation.
   */
  @Override
  public String toString() {
    return "DummyNode [data=" + data + "]";
  }

  /**
   * Generates a hashcode based on the Node data.
   *
   * @return An integer hashCode.
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + data;
    return result;
  }

  /**
   * Checks whether two Nodes are equal by checking equality of their data.
   *
   * @param obj
   *          Object to compare equality with.
   * @return A boolean whether or not they are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DummyNode other = (DummyNode) obj;
    if (data != other.data) {
      return false;
    }
    return true;
  }

}
