package edu.brown.cs.board;

import edu.brown.cs.catan.Resource;

public interface BoardTile {

  HexCoordinate getCoordinate();

  Resource getType();
  boolean hasRobber();
  int getRollNumber();


}
