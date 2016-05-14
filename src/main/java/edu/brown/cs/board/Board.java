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
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.catan.GameSettings;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Settings;

/**
 * Board Class. Functions as a container class for all of the board data.
 *
 * @author anselvahle
 *
 */
public class Board {
  private Collection<Tile> _tiles;
  private Map<IntersectionCoordinate, Intersection> _intersections;
  private Map<PathCoordinate, Path> _paths;
  private final List<HexCoordinate> PORT_LOCATION;
  private final static int DEPTH = 2;

  /**
   * Adds a certain number of tiles of a specific type to a list to shuffle when
   * constructing the board.
   *
   * @param availTiles
   *          List of tiles.
   * @param type
   *          Type of tiles to add.
   * @param numTiles
   *          How many tiles to add.
   */
  private void addTiles(List<TileType> availTiles, TileType type, int numTiles) {
    for (int i = 0; i < numTiles; i++) {
      availTiles.add(type);
    }
  }

  /**
   * Gets permutations of sea coordinates and puts them into a list.
   *
   * @return List of HexCoordinates of the location of Sea Tiles.
   */
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

  /**
   * Returns true if there are possible permutations of the array.
   *
   * @param data
   *          Array to permute
   * @return Boolean stating if there are more permutations
   */
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

  /**
   * Tells the tiles what was rolled.
   *
   * @param roll
   *          Num that was rolled.
   */
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

  /**
   * Getter for the tiles.
   * 
   * @return Collection of the tiles in the board.
   */
  public Collection<Tile> getTiles() {
    return Collections.unmodifiableCollection(_tiles);
  }

  /**
   * Getter for the intersections.
   *
   * @return Map of the intersections on the board.
   */
  public Map<IntersectionCoordinate, Intersection> getIntersections() {
    return Collections.unmodifiableMap(_intersections);
  }

  /**
   * Getter for the paths.
   *
   * @return Map of the paths in the board.
   */
  public Map<PathCoordinate, Path> getPaths() {
    return Collections.unmodifiableMap(_paths);
  }

  /**
   * Sets the port locations for a standard board size;
   * 
   * @return List of the coordinates of the port locations for the standard
   *         board.
   */
  private List<HexCoordinate> setPortLocations() {
    List<HexCoordinate> toRet = new ArrayList<HexCoordinate>();
    toRet.add(new HexCoordinate(0, 0, 3));
    toRet.add(new HexCoordinate(0, 2, 3));
    toRet.add(new HexCoordinate(0, 3, 2));
    toRet.add(new HexCoordinate(0, 3, 0));
    toRet.add(new HexCoordinate(2, 3, 0));
    toRet.add(new HexCoordinate(3, 2, 0));
    toRet.add(new HexCoordinate(3, 0, 0));
    toRet.add(new HexCoordinate(3, 0, 2));
    toRet.add(new HexCoordinate(2, 0, 3));
    return toRet;
  }

  /**
   * Constructor for the Board.
   *
   * @param settings
   *          Settings for how the board should be made.
   */
  public Board(GameSettings settings) {
    List<TileType> availTiles = new ArrayList<TileType>();
    int[] rollNums = ROLL_NUMS;
    // Determines whether the board should be random or not;
    if (settings.isStandard) {
      availTiles = standardBoard();
      rollNums = Settings.STANDARD_ROLL_NUMS;
    } else {
      addTiles(availTiles, WOOD, NUM_WOOD_TILE);
      addTiles(availTiles, BRICK, NUM_BRICK_TILE);
      addTiles(availTiles, SHEEP, NUM_SHEEP_TILE);
      addTiles(availTiles, WHEAT, NUM_WHEAT_TILE);
      addTiles(availTiles, ORE, NUM_ORE_TILE);
      addTiles(availTiles, DESERT, NUM_DESERT_TILE);
      do {
        Collections.shuffle(availTiles);
      } while ((availTiles.get(0) == DESERT));
    }
    // Sets the port locations
    PORT_LOCATION = setPortLocations();

    Map<IntersectionCoordinate, Intersection> intersections = new HashMap<IntersectionCoordinate, Intersection>();
    Map<PathCoordinate, Path> paths = new HashMap<PathCoordinate, Path>();
    _tiles = new ArrayList<>();
    int currRoll = 0;
    int currTile = 0;
    int currDepth = DEPTH;
    int x = DEPTH;
    int y = 0;
    int z = 0;
    int i = 0;
    // How it constructs the board
    while (currDepth >= 0) {
      currRoll = addTile(availTiles.get(currTile), new HexCoordinate(x, y, z),
          intersections, paths, currRoll, currTile, rollNums);
      currTile++;

      for (i = 0; i < currDepth; i++) {
        y++;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }

      for (i = 0; i < currDepth; i++) {
        x--;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }

      for (i = 0; i < currDepth; i++) {
        z++;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }

      for (i = 0; i < currDepth; i++) {
        y--;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }

      for (i = 0; i < currDepth; i++) {
        x++;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }

      for (i = 1; i < currDepth; i++) {
        z--;
        currRoll = addTile(availTiles.get(currTile),
            new HexCoordinate(x, y, z), intersections, paths, currRoll,
            currTile, rollNums);
        currTile++;
      }
      z--;
      x--;
      currDepth--;
    }

    _intersections = intersections;
    _paths = paths;
    i = 0;
    List<HexCoordinate> seaCoords = getSeaPermutations();
    // Adds sea tiles and ports
    for (HexCoordinate hc : seaCoords) {
      Tile seaTile = new Tile(hc, SEA, _intersections);
      if (PORT_LOCATION.contains(hc)) {
        seaTile.setPorts(new Port(
            Settings.PORT_ORDER[PORT_LOCATION.indexOf(hc)]));
        i++;
      }
      _tiles.add(seaTile);
    }
  }

  /**
   * 
   * @param tileType
   * @param coord
   * @param intersections
   * @param paths
   * @param currRoll
   * @param currTile
   * @param rollNums
   * @return
   */
  private int addTile(TileType tileType, HexCoordinate coord,
      Map<IntersectionCoordinate, Intersection> intersections,
      Map<PathCoordinate, Path> paths, Integer currRoll, Integer currTile,
      int[] rollNums) {
    if (tileType != DESERT) {
      _tiles.add(new Tile(rollNums[currRoll], coord, intersections, paths,
          tileType));
      return currRoll + 1;
    } else {
      _tiles.add(new Tile(0, coord, intersections, paths, tileType, true));
      return currRoll;
    }
  }

  public HexCoordinate findRobber() {
    for (Tile t : _tiles) {
      if (t.hasRobber()) {
        return t.getCoordinate();
      }
    }
    return null;
  }

  /**
   * Moves the robber to a given HexCoord.
   * 
   * @param coord
   *          Coordinate of the tile to moved it to.
   * @return Set of Integers that are the playerIDs of the people on the tile
   *         the robber was moved to.
   */
  public Set<Integer> moveRobber(HexCoordinate coord) {
    Set<Integer> playersOnTile = Collections.emptySet();
    for (Tile t : _tiles) {
      if (t.hasRobber()) {
        if (t.getCoordinate().equals(coord)) {
          throw new IllegalArgumentException(
              "The robber must be moved to a new location.");
        }
        t.hasRobber(false);
      } else if (t.getCoordinate().equals(coord)) {
        t.hasRobber(true);
        playersOnTile = t.getPlayersOnTile();
      }
    }
    return playersOnTile;
  }

  /**
   * Finds the longest path on the board.
   * 
   * @param player
   *          Finds the longest path belonging to this player.
   * @return The length of the players longest road.
   */
  public int longestPath(Player player) {
    int max = 0;
    for (Path path : _paths.values()) {
      int longestPath = path.getLongestPath(player);
      if (longestPath > max) {
        max = longestPath;
      }
    }
    return max;
  }

  // Standard Board
  private List<TileType> standardBoard() {
    List<TileType> tiles = new ArrayList<>();
    tiles.add(WHEAT);
    tiles.add(SHEEP);
    tiles.add(WOOD);
    tiles.add(BRICK);
    tiles.add(DESERT);
    tiles.add(BRICK);
    tiles.add(ORE);
    tiles.add(WHEAT);
    tiles.add(WOOD);
    tiles.add(ORE);
    tiles.add(WHEAT);
    tiles.add(SHEEP);
    tiles.add(BRICK);
    tiles.add(ORE);
    tiles.add(WOOD);
    tiles.add(SHEEP);
    tiles.add(SHEEP);
    tiles.add(WOOD);
    tiles.add(WHEAT);
    return tiles;
  }

}
