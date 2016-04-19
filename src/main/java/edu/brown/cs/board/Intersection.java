package edu.brown.cs.board;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class Intersection {

  private Building _building;
  private Port _port;
  private Collection<Path> _paths;
  private IntersectionCoordinate _position;
  
  public Intersection(Collection<Path> paths,
      IntersectionCoordinate position) {
    _paths = paths;
    _position = position;
    _building = null;
    _port = null;
  }
  
  public Intersection(IntersectionCoordinate position) {
    _paths = new ArrayList<Path>();
    _position = position;
    _building = null;
    _port = null;
  }

  public Intersection(Collection<Path> paths,
      IntersectionCoordinate position, Port port) {
    _paths = paths;
    _position = position;
    _building = null;
    _port = port;
  }

  public Intersection(IntersectionCoordinate position,
      Port port) {
    _paths = new ArrayList<Path>();
    _position = position;
    _building = null;
    _port = port;
  }

  public void addPath(Path path) {
    _paths.add(path);
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

}
