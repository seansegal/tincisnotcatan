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
    start.addPath(this);
    end.addPath(this);
  }

  public Road getRoad() {
    return _road;
  }

  @Override
  public int hashCode() {
    return 1;
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
    Path other = (Path) obj;
    if (_end == null || _start == null) {
      if (other._end != null  || other._start != null) {
        return false;
      }
    } else if (!((_end.equals(other._end) && _start.equals(other._start))) || (_end.equals(other._start) && _start.equals(other._end))) {
      return false;
    }
    return true;
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
