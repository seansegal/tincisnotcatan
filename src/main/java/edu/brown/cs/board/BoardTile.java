package edu.brown.cs.board;

import java.util.List;
import java.util.Set;

import edu.brown.cs.catan.Resource;

/**
 * Board Tile interface, for types of tiles on the board.
 *
 * @author anselvahle
 *
 */
public interface BoardTile {

  /**
   * Gets the coordinate of the Tile.
   *
   * @return HexCoordinate of the Tile.
   */
  HexCoordinate getCoordinate();

  /**
   * Gets the type of the tile.
   *
   * @return Type of the tile.
   */
  TileType getType();

  /**
   * States whether or not the robber is on the tile.
   *
   * @return Boolean stating whether or not the robber is on the tile.
   */
  boolean hasRobber();

  /**
   * Returns the number associated with the tile.
   *
   * @return Int that is the roll number on the tile.
   */
  int getRollNumber();

  /**
   * Gets the Port locations (which intersections) from this tile.
   *
   * @return List of the Intersection coordinates of the intersections where
   *         ports are.
   */
  List<IntersectionCoordinate> getPortLocations();

  /**
   * Gets the type of the port that is on this tile.
   * 
   * @return Type of port, else null;
   */
  Resource getPortType();

  /**
   * Gets the player IDs who have intersections on the tile.
   *
   * @return a set of ints that is the player IDs.
   */
  Set<Integer> getPlayersOnTile();
}
