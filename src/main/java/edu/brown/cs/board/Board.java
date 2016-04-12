package edu.brown.cs.board;

import static edu.brown.cs.board.TileType.BRICK;
import static edu.brown.cs.board.TileType.DESERT;
import static edu.brown.cs.board.TileType.ORE;
import static edu.brown.cs.board.TileType.SHEEP;
import static edu.brown.cs.board.TileType.WHEAT;
import static edu.brown.cs.board.TileType.WOOD;
import static edu.brown.cs.catan.Settings.NUM_BRICK_TILE;
import static edu.brown.cs.catan.Settings.NUM_DESERT_TILE;
import static edu.brown.cs.catan.Settings.NUM_ORE_TILE;
import static edu.brown.cs.catan.Settings.NUM_SHEEP_TILE;
import static edu.brown.cs.catan.Settings.NUM_WHEAT_TILE;
import static edu.brown.cs.catan.Settings.NUM_WOOD_TILE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Board {
  private Collection<Tile> _tiles;
  private int _robberTile;

  public Board() {
    List<TileType> availTiles = new ArrayList<TileType>();
    int i;
    for (i = 0; i < NUM_WOOD_TILE; i++) {
      availTiles.add(WOOD);
    }
    for (i = 0; i < NUM_BRICK_TILE; i++) {
      availTiles.add(BRICK);
    }
    for (i = 0; i < NUM_SHEEP_TILE; i++) {
      availTiles.add(SHEEP);
    }
    for (i = 0; i < NUM_WHEAT_TILE; i++) {
      availTiles.add(WHEAT);
    }
    for (i = 0; i < NUM_ORE_TILE; i++) {
      availTiles.add(ORE);
    }
    for (i = 0; i < NUM_DESERT_TILE; i++) {
      availTiles.add(DESERT);
    }
    Collections.shuffle(availTiles);
  }

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
