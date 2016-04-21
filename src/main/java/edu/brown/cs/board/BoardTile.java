package edu.brown.cs.board;


public interface BoardTile {

  HexCoordinate getCoordinate();

  TileType getType();
  boolean hasRobber();
  int getRollNumber();


}
