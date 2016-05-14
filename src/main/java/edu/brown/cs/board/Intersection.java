package edu.brown.cs.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Referee.GameStatus;
import edu.brown.cs.catan.Resource;

public class Intersection {
  private List<Path> _paths;
  private Building _building;
  private Port _port;
  private IntersectionCoordinate _position;

  /**
   * Constructor for the class.
   *
   * @param position
   *          Intersection Coordinate that represents this intersection's
   *          position on the board.
   */
  public Intersection(IntersectionCoordinate position) {
    _position = position;
    _building = null;
    _port = null;
    _paths = new ArrayList<Path>();
  }

  @Override
  public String toString() {
    return "Intersection [_position=" + _position + "]";
  }

  /**
   * Gets the port associated with this intersection.
   *
   * @return The port associated with this intersection, else null.
   */
  public Port getPort() {
    return _port;
  }

  /**
   * Gets the coordinate of this intersection.
   *
   * @return IntersectionCoordinate for this intersection.
   */
  public IntersectionCoordinate getPosition() {
    return _position;
  }

  /**
   * Tells the building to tell the player who is associated with it to collect
   * a resource of the input type.
   *
   * @param res
   *          Type of resource to collect.
   * @return A map of the player id to a map of resources that they collected
   *         and how many of them.
   */
  public Map<Integer, Map<Resource, Integer>> notifyBuilding(Resource res) {
    Map<Integer, Map<Resource, Integer>> toRet = new HashMap<Integer, Map<Resource, Integer>>();
    if (_building != null) {
      toRet = _building.collectResource(res);
    }
    return toRet;
  }

  /**
   * Places a settlement on this intersection associated with the input player.
   *
   * @param p
   *          Player who is wanting to build a settlement on the intersection.
   */
  public void placeSettlement(Player p) {
    if (_building == null) {
      _building = new Settlement(p);
    }
  }

  /**
   * Places a city on this intersection associated with the input player.
   *
   * @param p
   *          Player who is wanting to build a city on the intersection.
   */
  public void placeCity(Player p) {
    if (canPlaceCity(p)) {
      _building = new City(p);
    }
  }

  // States whether or not there is a settlement on an intersection 1 road
  // length away.
  private boolean hasAdjacentSettlement() {
    for (Path p : _paths) {
      if (p.getOtherEnd(this).getBuilding() != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * States whether or not a player can place a settlement on this intersection.
   *
   * @param r
   *          The referee of the game.
   * @param playerID
   *          ID of the player who is wanting to build a settlement
   * @return A boolean stating whether or not the player can build on this
   *         intersection.
   */
  public boolean canPlaceSettlement(Referee r, int playerID) {
    if (_building == null && !hasAdjacentSettlement()) {
      if (r.getGameStatus() != GameStatus.PROGRESS) {
        return true;
      } else {
        for (Path p : _paths) {
          if (p.getRoad() != null
              && p.getRoad().getPlayer().getID() == playerID) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * States whether or not a player can place a city on this intersection.
   *
   * @param p
   *          the player who is wanting to build a city
   * @return A boolean stating whether or not the player can build on this
   *         intersection.
   */
  public boolean canPlaceCity(Player p) {
    if (_building == null) {
      return false;
    } else if (_building.getPlayer().equals(p)
        && _building instanceof Settlement) {
      return true;
    }

    return false;
  }

  /**
   * Adds a path associated with this intersection.
   * 
   * @param p
   */
  public void addPath(Path p) {
    _paths.add(p);
  }

  /**
   * Gets the paths associated with this intersection.
   *
   * @return List of the paths associated with this intersection.
   */
  public List<Path> getPaths() {
    return _paths;
  }

  /**
   * Sets the port of this intersection.
   *
   * @param p
   *          Port to set.
   */
  public void setPort(Port p) {
    _port = p;
  }

  /**
   * Getter for the building on this intersection.
   * 
   * @return The building on this intersection, else null.
   */
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
      if (other._position != null) {
        return false;
      }
    } else if (!_position.equals(other._position)) {
      return false;
    }
    return true;
  }

}
