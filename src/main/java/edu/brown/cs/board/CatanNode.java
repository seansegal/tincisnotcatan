package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.brown.cs.graph.Edge;
import edu.brown.cs.graph.Node;

public class CatanNode implements Node<Path, Intersection> {
  private Intersection _data;
  private GraphCache _cache;

  public CatanNode(Intersection data, GraphCache cache) {
    _data = data;
    _cache = cache;
  }

  @Override
  public List<Edge<Path, Intersection>> getEdges() {
    Collection<Path> paths = _data.getPaths();
    List<Edge<Path, Intersection>> edges = new ArrayList<Edge<Path, Intersection>>();
    if(paths != null) {
      for(Path p : paths) {
        if (_cache.getEdge(p) != null) {
          edges.add(_cache.getEdge(p));
        } else {
          CatanNode start;
          if (_cache.getNode(p.getStart()) != null) {
             start = _cache.getNode(p.getStart());
          } else {
            start = new CatanNode(p.getStart(), _cache);
            _cache.addNode(p.getStart(), start);
          }
          CatanNode end;
          if(_cache.getNode(p.getEnd()) != null) {
            end = _cache.getNode(p.getEnd());
         } else {
           end = new CatanNode(p.getEnd(), _cache);
           _cache.addNode(p.getEnd(), end);
         }
          CatanEdge toAdd = new CatanEdge(start, end, p, 1.0);
          _cache.addEdge(p, toAdd);
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
