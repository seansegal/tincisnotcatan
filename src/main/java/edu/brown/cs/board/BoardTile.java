package edu.brown.cs.board;

import java.util.List;
import java.util.Set;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;


public interface BoardTile {

  HexCoordinate getCoordinate();

  TileType getType();
  boolean hasRobber();
  int getRollNumber();
  List<IntersectionCoordinate> getPortLocations();
  Resource getPortType();
  Set<Player> getPlayersOnTile();
}
