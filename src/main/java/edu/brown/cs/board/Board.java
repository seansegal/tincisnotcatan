package edu.brown.cs.board;

import java.util.Collection;

public interface Board {

  //instance variable: robberTile (int tileID)

  void notifyTiles(int roll);
  Collection<Tile> getTiles();
  void moveRobber(int tileID);


}
