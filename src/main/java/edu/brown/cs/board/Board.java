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
import static edu.brown.cs.catan.Settings.ROLL_NUMS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
  private Collection<Tile> _tiles;
  private int _robberTile;

  public Board() {
    List<TileType> availTiles = new ArrayList<TileType>();
    List<HexCoordinate> coords = getCoordPermutations();
    addTiles(availTiles, WOOD, NUM_WOOD_TILE);
    addTiles(availTiles, BRICK, NUM_BRICK_TILE);
    addTiles(availTiles, SHEEP, NUM_SHEEP_TILE);
    addTiles(availTiles, WHEAT, NUM_WHEAT_TILE);
    addTiles(availTiles, ORE, NUM_ORE_TILE);
    addTiles(availTiles, DESERT, NUM_DESERT_TILE);
    Collections.shuffle(availTiles);

    Map<IntersectionCoordinate, Intersection> intersections =
        new HashMap<IntersectionCoordinate, Intersection>();

    _tiles = new ArrayList<>();
    for (int i = 0; i < availTiles.size(); i++) {
      _tiles.add(new Tile(ROLL_NUMS[i], coords.get(i), intersections,
          availTiles.get(i)));
    }

  }

  private void addTiles(List<TileType> availTiles, TileType type, int numTiles) {
    for (int i = 0; i < numTiles; i++) {
      availTiles.add(type);
    }
  }

  private List<HexCoordinate> getCoordPermutations() {
    List<HexCoordinate> coords = new ArrayList<HexCoordinate>();
    coords.add(new HexCoordinate(0, 0, 0));
    int[] oneOne = { 0, 0, 1 };
    int[] twoOne = { 0, 1, 1 };
    int[] oneTwo = { 0, 0, 2 };
    int[] oneTwoOneOne = { 0, 1, 2 };
    int[] twoTwo = { 0, 2, 2 };

    coords.add(new HexCoordinate(oneOne[0], oneOne[1], oneOne[2]));
    while (permute(oneOne)) {
      coords.add(new HexCoordinate(oneOne[0], oneOne[1], oneOne[2]));
    }

    coords.add(new HexCoordinate(twoOne[0], twoOne[1], twoOne[2]));
    while (permute(twoOne)) {
      coords.add(new HexCoordinate(twoOne[0], twoOne[1], twoOne[2]));
    }

    coords.add(new HexCoordinate(oneTwo[0], oneTwo[1], oneTwo[2]));
    while (permute(oneTwo)) {
      coords.add(new HexCoordinate(oneTwo[0], oneTwo[1], oneTwo[2]));
    }

    coords.add(new HexCoordinate(oneTwoOneOne[0], oneTwoOneOne[1],
        oneTwoOneOne[2]));
    while (permute(oneTwoOneOne)) {
      coords.add(new HexCoordinate(oneTwoOneOne[0], oneTwoOneOne[1],
          oneTwoOneOne[2]));
    }

    coords.add(new HexCoordinate(twoTwo[0], twoTwo[1], twoTwo[2]));
    while (permute(twoTwo)) {
      coords.add(new HexCoordinate(twoTwo[0], twoTwo[1], twoTwo[2]));
    }
    return coords;
  }

  public static void main(String[] args) {
    Board b = new Board();
  }

  private boolean permute(int[] data) {
    int k = data.length - 2;
    while (data[k] >= data[k + 1]) {
      k--;
      if (k < 0) {
        return false;
      }
    }
    int l = data.length - 1;
    while (data[k] >= data[l]) {
      l--;
    }
    swap(data, k, l);
    int length = data.length - (k + 1);
    for (int i = 0; i < length / 2; i++) {
      swap(data, k + 1 + i, data.length - i - 1);
    }
    return true;
  }

  private void swap(int[] data, int idx1, int idx2) {
    int tmp = data[idx1];
    data[idx1] = data[idx2];
    data[idx2] = tmp;
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
