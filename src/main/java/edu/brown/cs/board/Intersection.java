package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class Intersection {
  private List<Path> _paths;
  private Building _building;
  private Port _port;
  private IntersectionCoordinate _position;

  public Intersection(IntersectionCoordinate position) {
    _position = position;
    _building = null;
    _port = null;
    _paths = new ArrayList<Path>();
  }

  public Port getPort() {
    return _port;
  }

  public IntersectionCoordinate getPosition() {
    return _position;
  }

  public void notifyBuilding(Resource res) {
    if (_building != null) {
      _building.collectResource(res);
    }
  }

  public void placeSettlement(Player p) {
    if (canPlaceSettlement(p)) {
      _building = new Settlement(p);
    }
  }

  public void placeCity(Player p) {
    if (canPlaceCity(p)) {
      _building = new City(p);
    }
  }

  boolean canPlaceSettlement(Player p) {
    if (_building == null) {
      return true;
    } else {
      return false;
    }
  }

  boolean canPlaceCity(Player p) {
    if (_building == null) {
      return false;
    } else if (_building.getPlayer().equals(p)
        && _building instanceof Settlement) { // Need to double check that type
                                              // checking will allow this to
        return true;                          // work

    }

    return false;
  }

  public void addPath(Path p) {
    _paths.add(p);
  }

  public List<Path> getPaths() {
    return _paths;
  }

  public void setPort(Port p) {
    _port = p;
  }

  public Building getBuilding() {
    return _building;
  }

  @Override
  public int hashCode() {
    return _position.hashCode();
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
    Intersection other = (Intersection) obj;
    if (_position == null) {
      if (other._position != null)
        return false;
    } else if (!_position.equals(other._position)) {
      return false;
    }
    return true;
  }

}
