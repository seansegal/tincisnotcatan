package edu.brown.cs.board;

import java.util.Collection;

public class Board {
  private Collection<Tile> _tiles;
  private int _robberTile;

  public void notifyTiles(int roll) {
    for (Tile t : _tiles) {
      if (t.getRollNum() == roll) {
        t.notifyIntersections();
      }
    }
  }

  public Collection<Tile> getTiles() {
    return _tiles;
  }
  
  public void moveRobber(int tileID) {
    assert (tileID != _robberTile);
    _robberTile = tileID;
  }


}
