package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
      if (other._end != null || other._start != null) {
        return false;
      }
    }
    if (((_end.equals(other._end) && _start.equals(other._start)))
        || (_end.equals(other._start) && _start.equals(other._end))) {
      return true;
    }
    return false;
  }

  public int getLongestPath(Player player) {
    Collection<Path> visited = new ArrayList<>();
    List<Path> queue = new ArrayList<>();
    Map<Path, Integer> counts = new HashMap<>();
    queue.add(this);
    counts.put(this, 0);
    while (!queue.isEmpty()) {
      Path toVisit = queue.remove(0);
      visited.add(toVisit);
      int curr = counts.get(toVisit) + 1;
      for (Path p : toVisit.getStart().getPaths()) {
        if (p.getRoad() != null && p.getRoad().getPlayer().equals(player)
            && !visited.contains(p)) {
          visited.add(p);
          counts.put(p, curr);
          queue.add(p);
        }
      }
      for (Path p : toVisit.getEnd().getPaths()) {
        if (p.getRoad() != null && p.getRoad().getPlayer().equals(player)
            && !visited.contains(p)) {
          visited.add(p);
          counts.put(p, curr);
          queue.add(p);
        }
      }
    }
    int max = 0;
    for (Path p : visited) {
      int longest = counts.get(p);
      if (longest > max) {
        max = longest;
      }
    }
    return max;
  }

  public boolean canPlaceRoad(Player p) {
    if (_road != null) {
      return false;
    }

    if (_start.getBuilding() != null
        && _start.getBuilding().getPlayer().equals(p)) {
      return true;
    } else if (_start.getBuilding() == null) {
      for (Path path : _start.getPaths()) {
        if (path.getRoad() != null && path.getRoad().getPlayer().equals(p)) {
          return true;
        }
      }
    }
    if (_end.getBuilding() != null && _end.getBuilding().getPlayer().equals(p)) {
      return true;
    } else if (_end.getBuilding() == null) {
      for (Path path : _end.getPaths()) {
        if (path.getRoad() != null && path.getRoad().getPlayer().equals(p)) {
          return true;
        }
      }
    }
    return false;
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

  @Override
  public String toString() {
    return _start + " <-->" + _end;
  }

}
