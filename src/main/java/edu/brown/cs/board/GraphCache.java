package edu.brown.cs.board;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for the Catan Graph.
 *
 * @author anselvahle
 *
 */
public class GraphCache {

  private final Map<Intersection, CatanNode> _nodes;
  private final Map<Path, CatanEdge> _edges;

  /**
   * Constructor for the class
   */
  public GraphCache() {
    _nodes = new ConcurrentHashMap<Intersection, CatanNode>();
    _edges = new ConcurrentHashMap<Path, CatanEdge>();
  }

  /**
   * Gets a node for the given intersection.
   *
   * @param i
   * @return
   */
  public CatanNode getNode(Intersection i) {
    return _nodes.get(i);
  }

  /**
   * Adds a node associated with the input intersection into the cache.
   *
   * @param i
   *          Intersection associated with the node.
   * @param cn
   *          CatanNode to add to the cache.
   */
  public void addNode(Intersection i, CatanNode cn) {
    _nodes.put(i, cn);
  }

  /**
   * Gets an edge associated with a specific path.
   *
   * @param p
   *          Path associated with the edge.
   * @return The edge associated with the input path.
   */
  public CatanEdge getEdge(Path p) {
    return _edges.get(p);
  }

  /**
   * Adds an edge associated with the input path to the cache.
   *
   * @param p
   *          Path associated the the Edge.
   * @param ce
   *          CatanEdge to add to the cache.
   */
  public void addEdge(Path p, CatanEdge ce) {
    _edges.put(p, ce);
  }
}
