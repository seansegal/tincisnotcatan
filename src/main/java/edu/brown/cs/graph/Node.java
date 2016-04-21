package edu.brown.cs.graph;

import java.util.List;

/**
 *
 * Represents a Node in our Graph representation. Any Class that implements
 * these methods can have Graphs functions run on them.
 *
 * @param <E>
 *          Edge data type
 * @param <N>
 *          Node data type
 */
public interface Node<E, N> {

  /**
   * Returns all outgoing edges from the Node.
   *
   * @return List of all outgoing edges.
   */
  List<Edge<E, N>> getEdges();

  /**
   * Returns the generic data associated with the Node.
   *
   * @return The Node's data.
   */
  N getData();

}
