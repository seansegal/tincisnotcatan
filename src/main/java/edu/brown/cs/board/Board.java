package edu.brown.cs.board;

import static edu.brown.cs.board.TileType.BRICK;
import static edu.brown.cs.board.TileType.DESERT;
import static edu.brown.cs.board.TileType.ORE;
import static edu.brown.cs.board.TileType.SEA;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.brown.cs.catan.Settings;

public class Board {
  private Collection<Tile> _tiles;
  private Map<IntersectionCoordinate, Intersection> _intersections;
  private Map<PathCoordinate, Path> _paths;
  private final Collection<HexCoordinate> PORT_LOCATION;
  private final static int DEPTH = 2;

  private void addTiles(List<TileType> availTiles, TileType type, int numTiles) {
    for (int i = 0; i < numTiles; i++) {
      availTiles.add(type);
    }
  }

  private List<HexCoordinate> getResourcePermutations() {
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

  private List<HexCoordinate> getSeaPermutations() {
    List<HexCoordinate> coords = new ArrayList<HexCoordinate>();
    int[] oneThree = { 0, 0, 3 };
    int[] oneThreeOneTwo = { 0, 2, 3 };
    int[] oneThreeOneOne = { 0, 1, 3 };
    int[] twoThree = { 0, 3, 3 };

    coords.add(new HexCoordinate(oneThree[0], oneThree[1], oneThree[2]));
    while (permute(oneThree)) {
      coords.add(new HexCoordinate(oneThree[0], oneThree[1], oneThree[2]));
    }

    coords.add(new HexCoordinate(oneThreeOneTwo[0], oneThreeOneTwo[1],
        oneThreeOneTwo[2]));
    while (permute(oneThreeOneTwo)) {
      coords.add(new HexCoordinate(oneThreeOneTwo[0], oneThreeOneTwo[1],
          oneThreeOneTwo[2]));
    }

    coords.add(new HexCoordinate(oneThreeOneOne[0], oneThreeOneOne[1],
        oneThreeOneOne[2]));
    while (permute(oneThreeOneOne)) {
      coords.add(new HexCoordinate(oneThreeOneOne[0], oneThreeOneOne[1],
          oneThreeOneOne[2]));
    }

    coords.add(new HexCoordinate(twoThree[0], twoThree[1], twoThree[2]));
    while (permute(twoThree)) {
      coords.add(new HexCoordinate(twoThree[0], twoThree[1], twoThree[2]));
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
      if (t.getRollNumber() == roll) {
        t.notifyIntersections();
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder toRet = new StringBuilder();
    toRet.append(_tiles.toString());
    toRet.append("\n");
    toRet.append(_intersections.keySet().size());
    toRet.append("\n");
    toRet.append(_paths.keySet().size());
    return toRet.toString();
  }

  public Collection<Tile> getTiles() {
    return Collections.unmodifiableCollection(_tiles);
  }

  public Map<IntersectionCoordinate, Intersection> getIntersections() {
    return Collections.unmodifiableMap(_intersections);
  }

  public Map<PathCoordinate, Path> getPaths() {
    return Collections.unmodifiableMap(_paths);
  }

  public Board() {
    List<TileType> availTiles = new ArrayList<TileType>();
    PORT_LOCATION = new HashSet<HexCoordinate>();
    PORT_LOCATION.add(new HexCoordinate(0, 0, 3));
    PORT_LOCATION.add(new HexCoordinate(0, 2, 3));
    PORT_LOCATION.add(new HexCoordinate(0, 3, 2));
    PORT_LOCATION.add(new HexCoordinate(0, 3, 0));
    PORT_LOCATION.add(new HexCoordinate(2, 3, 0));
    PORT_LOCATION.add(new HexCoordinate(3, 2, 0));
    PORT_LOCATION.add(new HexCoordinate(3, 0, 0));
    PORT_LOCATION.add(new HexCoordinate(3, 0, 2));
    PORT_LOCATION.add(new HexCoordinate(2, 0, 3));

    addTiles(availTiles, WOOD, NUM_WOOD_TILE);
    addTiles(availTiles, BRICK, NUM_BRICK_TILE);
    addTiles(availTiles, SHEEP, NUM_SHEEP_TILE);
    addTiles(availTiles, WHEAT, NUM_WHEAT_TILE);
    addTiles(availTiles, ORE, NUM_ORE_TILE);
    addTiles(availTiles, DESERT, NUM_DESERT_TILE);
    Collections.shuffle(availTiles);

    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();
    _tiles = new ArrayList<>();
    int currRoll = 0;
    int currTile = 0;
    int x = 0;
    int y = 0;
    int z = 0;
    // (0,0,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z++; // (0,0,1)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x++; // (1,0,1)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z--; // // (1,0,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    y++; // (1,1,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x--; // (0,1,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z++; // (0,1,1)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    y--; // (0,0,1)
    z++; // (0,0,2)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x++; // (1,0,2)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x++; // (2,0,2)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z--; // (2,0,1)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z--; // (2,0,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    y++; // (2,1,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    y++; // (2,2,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x--; // (1,2,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    x--; // (0,2,0)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z++; // (0,2,1)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    z++; // (0,2,2)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;
    y--; // (0,1,2)
    addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
        intersections, paths, currRoll, currTile);
    currTile ++; currRoll++;

    _intersections = intersections;
    _paths = paths;
    int i = 0;
    List<HexCoordinate> seaCoords = getSeaPermutations();
    for (HexCoordinate hc : seaCoords) {
      Tile seaTile = new Tile(hc, SEA, _intersections);
      if (PORT_LOCATION.contains(hc)) {
        seaTile.setPorts(new Port(Settings.PORT_ORDER[i]));
        i++;
      }
      _tiles.add(seaTile);
    }
  }

  private void addTile(TileType tileType, HexCoordinate coord,
      Map<IntersectionCoordinate, Intersection> intersections,
      Map<PathCoordinate, Path> paths, int currRoll, int currTile) {
    if (tileType != DESERT) {
      _tiles.add(new Tile(ROLL_NUMS[currRoll], coord, intersections,
          paths,tileType));
    } else {
      _tiles.add(new Tile(0, coord, intersections, paths, tileType, true));
    }
  }
}
