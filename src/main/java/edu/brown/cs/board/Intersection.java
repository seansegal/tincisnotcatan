package edu.brown.cs.board;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class Intersection {

  private Building _building;
  private int _intersectionID;
  private Port _port;
  private Collection<Path> _paths;
  private Collection<HexCoordinate> _position;
  
  public Intersection(int intersectionID, Collection<Path> paths,
      Collection<HexCoordinate> position) {
    _intersectionID = intersectionID;
    _paths = paths;
    _position = position;
    _building = null;
    _port = null;
  }
  
  public Intersection(int intersectionID, Collection<HexCoordinate> position) {
    _intersectionID = intersectionID;
    _paths = new ArrayList<Path>();
    _position = position;
    _building = null;
    _port = null;
  }

  public Intersection(int intersectionID, Collection<Path> paths,
      Collection<HexCoordinate> position, Port port) {
    _intersectionID = intersectionID;
    _paths = paths;
    _position = position;
    _building = null;
    _port = port;
  }

  public Intersection(int intersectionID, Collection<HexCoordinate> position,
      Port port) {
    _intersectionID = intersectionID;
    _paths = new ArrayList<Path>();
    _position = position;
    _building = null;
    _port = port;
  }

  public void addPath(Path path) {
    _paths.add(path);
  }

  public int getID() {
    return _intersectionID;
  }

  public Port getPort() {
    return _port;
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
