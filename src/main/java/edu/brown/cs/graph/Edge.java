package edu.brown.cs.graph;

/**
 *
 * Class that represents an Edge in this Graph implementation. In this
 * implementation Nodes must be able to return all their edges. This is a basic
 * Edge implementation that can be extended to add extra functionality. By
 * default the Edge Weights are doubles but this can be changed if subclassed.
 *
 * @param <E>
 *          Edge Data type
 * @param <N>
 *          Node Data type
 */
public class Edge<E, N> {

  private final double weight;
  private final E data;
  private final Node<E, N> node1;
  private final Node<E, N> node2;

  /**
   * Creates a Graph Edge.
   *
   * @param node1
   *          A node, or starting node in a directed graph
   * @param node2
   *          A node, or ending node in a directed graph
   * @param data
   *          Some generic data
   * @param weight
   *          Edge weight
   */
  public Edge(Node<E, N> node1, Node<E, N> node2, E data, double weight) {
    if (node1 == null || node2 == null) {
      throw new IllegalArgumentException("Nodes cannot be null.");
    }
    this.node1 = node1;
    this.node2 = node2;
    this.data = data;
    this.weight = weight;
  }

  /**
   * Returns the starting node (or just a node in a non-directed graph).
   *
   * @return A Node
   */
  public Node<E, N> getNode1() {
    return node1;
  }

  /**
   * Returns the ending node (or just a node in a non-directed graph).
   *
   * @return A Node.
   */
  public Node<E, N> getNode2() {
    return node2;
  }

  /**
   * Returns the weight associated with the edge.
   *
   * @return A double value, the edge weight.
   */
  public double getWeight() {
    return weight;
  }

  /**
   * The generic data associated with an Edge.
   *
   * @return The Edge's data.
   */
  public E getData() {
    return data;
  }

  /**
   * Returns a the opposite node from that inputted.
   *
   * @param node
   *          The node opposite to the one we want returned.
   * @return The adjacent Node
   */
  public Node<E, N> getAdjacent(Node<E, N> node) {
    if (!node.equals(node1) && !node.equals(node2)) {
      throw new IllegalArgumentException("Must input one of the Edge's nodes");
    }
    if (node.equals(node1)) {
      return node2;
    }
    return node1;
  }

}
