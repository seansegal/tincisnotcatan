package edu.brown.cs.board;

import edu.brown.cs.catan.Player;
import edu.brown.cs.graph.Edge;
import edu.brown.cs.graph.Node;

public class Path extends Edge<Path, Intersection> {

  public Path(Node<Path, Intersection> node1, Node<Path, Intersection> node2,
      Path data, double weight) {
    super(node1, node2, data, weight);
    // TODO Auto-generated constructor stub
  }

  public boolean canPlaceRoad(Player p) {
    // TODO
    return false;
  }

  public void placeRoad(Player p) {
    // TODO

  }

}
