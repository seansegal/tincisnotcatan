package edu.brown.cs.board;

import edu.brown.cs.graph.Edge;
import edu.brown.cs.graph.Node;

public class CatanEdge extends Edge<Path, Intersection> {

  public CatanEdge(Node<Path, Intersection> node1,
      Node<Path, Intersection> node2, Path data, double weight) {
    super(node1, node2, data, weight);
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
    CatanEdge other = (CatanEdge) obj;
    if (this.getData() == null) {
      if (other.getData() != null)
        return false;
    } else if (!this.getData().equals(other.getData())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return this.getData().hashCode();
  }

}
