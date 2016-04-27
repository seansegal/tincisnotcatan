package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class Tile implements BoardTile {
  private final Collection<Intersection> _intersections;
  private final int _rollNum;
  private final TileType _type;
  private final HexCoordinate _coordinate;
  private boolean _hasRobber;
  private List<IntersectionCoordinate> _portLocations;
  private Resource _portType;
  
  public Tile(int rollNum, HexCoordinate coordinate,
      Map<IntersectionCoordinate, Intersection> intersections,
      Map<PathCoordinate, Path> paths, TileType type) {
    _type = type;
    _rollNum = rollNum;
    _coordinate = coordinate;
    _hasRobber = false;
    _intersections = new ArrayList<Intersection>();
    _portLocations = new ArrayList<IntersectionCoordinate>();
    fillEdges(intersections, paths);
  }

  public Tile(int rollNum, HexCoordinate coordinate,
      Map<IntersectionCoordinate, Intersection> intersections,
      Map<PathCoordinate, Path> paths, TileType type, boolean hasRobber) {
    _type = type;
    _rollNum = rollNum;
    _coordinate = coordinate;
    _hasRobber = hasRobber;
    _intersections = new ArrayList<Intersection>();
    _portLocations = new ArrayList<IntersectionCoordinate>();
    fillEdges(intersections, paths);
  }

  public Tile(HexCoordinate coordinate, TileType type,
      Map<IntersectionCoordinate, Intersection> intersections) {
    _type = type;
    _rollNum = 0;
    _coordinate = coordinate;
    _hasRobber = false;
    _intersections = new ArrayList<Intersection>();
    _portLocations = new ArrayList<IntersectionCoordinate>();
    fillSeaTile(intersections);
  }

  private void fillSeaTile(Map<IntersectionCoordinate, Intersection> intersections) {
    PriorityQueue<IntersectionCoordinate> closestIntersections = 
        new PriorityQueue<>(6, new IntersectionComparator());

    HexCoordinate upLeftTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY(), _coordinate.getZ() + 1);
    HexCoordinate upRightTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY() + 1, _coordinate.getZ() + 1);
    HexCoordinate rightTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY() + 1, _coordinate.getZ());
    HexCoordinate lowerRightTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY() + 1, _coordinate.getZ());
    HexCoordinate lowerLeftTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY(), _coordinate.getZ());
    HexCoordinate leftTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY(), _coordinate.getZ() + 1);

    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        upLeftTile, upRightTile));
    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        upRightTile, rightTile));
    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        rightTile, lowerRightTile));
    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        lowerRightTile, lowerLeftTile));
    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        lowerLeftTile, leftTile));
    closestIntersections.add(new IntersectionCoordinate(_coordinate,
        leftTile, upLeftTile));

    // Add the two intersections closest to the origin
    IntersectionCoordinate toAdd = closestIntersections.poll();
    assert (intersections.containsKey(toAdd));
    _intersections.add(intersections.get(toAdd));

    toAdd = closestIntersections.poll();
    assert (intersections.containsKey(toAdd));
    _intersections.add(intersections.get(toAdd));
  }

  private static class IntersectionComparator implements
      Comparator<IntersectionCoordinate> {

    @Override
    public int compare(IntersectionCoordinate o1, IntersectionCoordinate o2) {
      double o1X = averagePositionX(o1);
      double o2X = averagePositionX(o2);
      double o1Y = averagePositionY(o1);
      double o2Y = averagePositionY(o2);
      
      return Double.compare(Math.pow(o1X, 2) + Math.pow(o1Y, 2),
          Math.pow(o2X, 2) + Math.pow(o2Y, 2));
    }

    private double averagePositionX(IntersectionCoordinate coord) {
      double x1 = coord.getCoord1().cartesianX();
      double x2 = coord.getCoord2().cartesianX();
      double x3 = coord.getCoord3().cartesianX();
      return (x1 + x2 + x3) / 3.0;
    }

    private double averagePositionY(IntersectionCoordinate coord) {
      double y1 = coord.getCoord1().cartesianY();
      double y2 = coord.getCoord2().cartesianY();
      double y3 = coord.getCoord3().cartesianY();
      return (y1 + y2 + y3) / 3.0;
    }

  }

  private void fillEdges(
      Map<IntersectionCoordinate, Intersection> intersections, Map<PathCoordinate, Path> paths) {
    HexCoordinate upLeftTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY(), _coordinate.getZ() + 1);
    HexCoordinate upRightTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY() + 1, _coordinate.getZ() + 1);
    HexCoordinate rightTile = new HexCoordinate(_coordinate.getX(),
        _coordinate.getY() + 1, _coordinate.getZ());
    HexCoordinate lowerRightTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY() + 1, _coordinate.getZ());
    HexCoordinate lowerLeftTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY(), _coordinate.getZ());
    HexCoordinate leftTile = new HexCoordinate(_coordinate.getX() + 1,
        _coordinate.getY(), _coordinate.getZ() + 1);
    
    IntersectionCoordinate top = new IntersectionCoordinate(_coordinate,
        upLeftTile, upRightTile);
    IntersectionCoordinate upRight = new IntersectionCoordinate(_coordinate,
        upRightTile, rightTile);
    IntersectionCoordinate lowerRight = new IntersectionCoordinate(_coordinate,
        rightTile, lowerRightTile);
    IntersectionCoordinate bottom = new IntersectionCoordinate(_coordinate,
        lowerRightTile, lowerLeftTile);
    IntersectionCoordinate lowerLeft = new IntersectionCoordinate(_coordinate,
        lowerLeftTile, leftTile);
    IntersectionCoordinate upLeft = new IntersectionCoordinate(_coordinate,
        leftTile, upLeftTile);

    fillIntersections(intersections, top);
    fillIntersections(intersections, upRight);
    fillIntersections(intersections, lowerRight);
    fillIntersections(intersections, bottom);
    fillIntersections(intersections, lowerLeft);
    fillIntersections(intersections, upLeft);
    
    fillPaths(intersections.get(top), intersections.get(upRight), paths);
    fillPaths(intersections.get(upRight), intersections.get(lowerRight), paths);
    fillPaths(intersections.get(lowerRight), intersections.get(bottom), paths);
    fillPaths(intersections.get(bottom), intersections.get(lowerLeft), paths);
    fillPaths(intersections.get(lowerLeft), intersections.get(upLeft), paths);
    fillPaths(intersections.get(upLeft), intersections.get(top), paths);

  }

  private void fillIntersections(
      Map<IntersectionCoordinate, Intersection> intersections,
      IntersectionCoordinate coord) {
    if(intersections.containsKey(coord)) {
      _intersections.add(intersections.get(coord));
    } else {
      Intersection newIntersect = new Intersection(coord);
      intersections.put(coord, newIntersect);
      _intersections.add(newIntersect);
    }
  }

  private void fillPaths(Intersection start, Intersection end,
      Map<PathCoordinate, Path> paths) {
    PathCoordinate path = new PathCoordinate(start.getPosition(), end.getPosition());
    if(!paths.containsKey(path)) {
      paths.put(path, new Path(start, end));
    }
  }

  @Override
  public TileType getType() {
    return _type;
  }

  public Map<Integer, Map<Resource, Integer>> notifyIntersections() {
    Map<Integer, Map<Resource, Integer>> playerResourceCount = new HashMap<Integer, Map<Resource, Integer>>();
    assert (_type.getType() != null);
    for (Intersection i : _intersections) {
      Map<Integer, Map<Resource, Integer>> fromInter = i.notifyBuilding(_type
          .getType());
      for (int playerID : fromInter.keySet()) {
        if (!playerResourceCount.containsKey(playerID)) {
          playerResourceCount.put(playerID, new HashMap<Resource, Integer>());
        }
        Map<Resource, Integer> resourceCount = fromInter.get(playerID);
        Map<Resource, Integer> playerCount = playerResourceCount.get(playerID);
        for(Resource res : resourceCount.keySet()) {
          if (playerCount.containsKey(res)) {
            playerCount.replace(res,
                playerCount.get(res) + resourceCount.get(res));
          } else {
            playerCount.put(res, resourceCount.get(res));
          }
        }
      }
    }

    return playerResourceCount;
  }

  public void setPorts(Port p) {
    assert (_type == TileType.SEA);
    for (Intersection i : _intersections) {
      i.setPort(p);
      _portLocations.add(i.getPosition());
    }
    _portType = p.getResource();
  }

  @Override
  public Resource getPortType() {
    return _portType;
  }

  @Override
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

  @Override
  public String toString() {
    StringBuilder toRet = new StringBuilder();
    toRet.append("TILETYPE: " + _type);
    toRet.append(" ROLL NUM: " + _rollNum + "\n");
    return toRet.toString();
  }

  @Override
  public boolean hasRobber() {
    return _hasRobber;
  }

  public void hasRobber(boolean _hasRobber) {
    this._hasRobber = _hasRobber;
  }

  @Override
  public int getRollNumber() {
    return _rollNum;
  }

  @Override
  public List<IntersectionCoordinate> getPortLocations() {
    return _portLocations;
  }

  @Override
  public Set<Player> getPlayersOnTile() {
    Set<Player> players = new HashSet<Player>();
    for (Intersection i : _intersections) {
      if (i.getBuilding() != null) {
        players.add(i.getBuilding().getPlayer());
      }
    }
    Set<Player> toRet = new ImmutableSet.Builder<Player>()
        .addAll(players).build();
    return toRet;
  }

}
