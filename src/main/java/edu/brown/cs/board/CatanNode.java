package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.brown.cs.graph.Edge;
import edu.brown.cs.graph.Node;

public class CatanNode implements Node<Path, Intersection> {
  private Intersection _data;

  public CatanNode(Intersection data) {
    _data = data;
  }

  @Override
  public List<Edge<Path, Intersection>> getEdges() {
    Collection<Path> paths = _data.getPaths();
    List<Edge<Path, Intersection>> edges = new ArrayList<Edge<Path, Intersection>>();
    if(paths != null) {
      for(Path p : paths) {
        if (GraphCache.edges.containsKey(p)) {
          edges.add(GraphCache.edges.get(p));
        } else {
          CatanNode start;
          if(GraphCache.nodes.containsKey(p.getStart())) {
             start = GraphCache.nodes.get(p.getStart());
          } else {
            start = new CatanNode(p.getStart());
            GraphCache.nodes.put(p.getStart(), start);
          }
          CatanNode end;
          if(GraphCache.nodes.containsKey(p.getEnd())) {
            end = GraphCache.nodes.get(p.getEnd());
         } else {
           end = new CatanNode(p.getEnd());
           GraphCache.nodes.put(p.getEnd(), end);
         }
          CatanEdge toAdd = new CatanEdge(start, end, p, 1.0);
          GraphCache.edges.put(p, toAdd);
          edges.add(toAdd);
        }
      }
    }
    return edges;
  }

  @Override
  public Intersection getData() {
    return _data;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_data == null) ? 0 : _data.hashCode());
    return result;
  }

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
    CatanNode other = (CatanNode) obj;
    if (_data == null) {
      if (other._data != null)
        return false;
    } else if (!_data.equals(other._data)) {
      return false;
    }
    return true;
  }
  


}
