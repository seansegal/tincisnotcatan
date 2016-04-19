package edu.brown.cs.board;

import edu.brown.cs.catan.Player;

public class Path {
  private Intersection _start;
  private Intersection _end;
  private Road _road;

  public Path(Intersection start, Intersection end) {
    _start = start;
    _end = end;
    _road = null;
  }

  public boolean canPlaceRoad(Player p) {
    return _road == null;
  }

  public void placeRoad(Player p) {
    if (canPlaceRoad(p)) {
      _road = new Road(p);
    }
  }

  public Intersection getStart() {
    return _start;
  }

  public Intersection getEnd() {
    return _end;
  }

  public Intersection getOtherEnd(Intersection end) {
    if (end.equals(_start)) {
      return _end;
    } else if (end.equals(_end)) {
      return _start;
    } else {
      throw new IllegalArgumentException();
    }
  }

}
