package edu.brown.cs.board;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GraphCache {

  private final Map<Intersection, CatanNode> _nodes;
  private final Map<Path, CatanEdge> _edges;

  public GraphCache() {
    _nodes = new ConcurrentHashMap<Intersection, CatanNode>();
    _edges = new ConcurrentHashMap<Path, CatanEdge>();
  }

  public CatanNode getNode(Intersection i) {
    return _nodes.get(i);
  }

  public void addNode(Intersection i, CatanNode cn) {
    _nodes.put(i, cn);
  }

  public CatanEdge getEdge(Path p) {
    return _edges.get(p);
  }

  public void addEdge(Path p, CatanEdge ce) {
    _edges.put(p, ce);
  }
}
