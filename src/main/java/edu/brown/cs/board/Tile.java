package edu.brown.cs.board;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs.catan.Resource;

public class Tile {
  private int _tileID;
  private Collection<Intersection> _intersections;
  private int _rollNum;
  private Resource _type;
  private HexCoordinate _coordinate;
  
  public Tile(int tileID, int rollNum, HexCoordinate coordinate,
      Map<IntersectionCoordinate, Intersection> intersections, TileType type) {
    _type = type.getType();
    _tileID = tileID;
    _rollNum = rollNum;
    _coordinate = coordinate;
    _intersections = intersections.values();
  }

  public int getID() {
    return _tileID;
  }

  public int getRollNum() {
    return _rollNum;
  }

  public Resource getType() {
    return _type;
  }

  public void notifyIntersections() {
    for (Intersection i : _intersections) {
      i.notifyBuilding(_type);
    }
  }

  public HexCoordinate getCoordinate() {
    return _coordinate;
  }

  public Collection<Intersection> getIntersections() {
    return _intersections;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Tile) {
      Tile other = (Tile) obj;
      if (other.getCoordinate().equals(_coordinate)) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return _coordinate.hashCode();
  }

}
