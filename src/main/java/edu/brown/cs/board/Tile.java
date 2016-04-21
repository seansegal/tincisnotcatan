package edu.brown.cs.board;

import java.util.Collection;

public interface Tile {

  void notifyIntersections();
  Collection<Intersection> getIntersections();

}
