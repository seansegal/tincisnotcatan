package edu.brown.cs.board;

import java.util.List;


public interface BoardTile {

  HexCoordinate getCoordinate();

  TileType getType();
  boolean hasRobber();
  int getRollNumber();
  List<IntersectionCoordinate> getPortLocations();

}
