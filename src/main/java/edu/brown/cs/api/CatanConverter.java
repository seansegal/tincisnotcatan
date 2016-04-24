package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.board.Board;
import edu.brown.cs.board.BoardTile;
import edu.brown.cs.board.Building;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.board.Path;
import edu.brown.cs.board.Port;
import edu.brown.cs.board.Road;
import edu.brown.cs.board.Tile;
import edu.brown.cs.board.TileType;
import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class CatanConverter {

  private Gson _gson;

  public CatanSettings getSettings(String settings) {
    try {
      return _gson.fromJson(settings, CatanSettings.class);
    } catch (Exception e) { // TODO: change to something better?
      throw new IllegalArgumentException("Could not parse settings JSON.");
    }

  }

  public CatanConverter() {
    _gson = new Gson();
  }

  public String getGameState(Referee ref, int playerID) {
    return _gson.toJson(new GameState(ref, playerID));
  }

  public String getBoard(Board board) {
    return _gson.toJson(new BoardRaw(board));
  }

  public String getHand(Player player) {
    return _gson.toJson(new Hand(player));
  }

  public String getPlayers(Referee referee) {
    List<PublicPlayerRaw> players = new ArrayList<>();
    for (Player p : referee.getPlayers()) {
      players.add(new PublicPlayerRaw(p, referee.getReadOnlyReferee()));
    }
    return _gson.toJson(ImmutableMap.of("players", players));
  }

  public Map<Integer, String> responseToJSON(
      Map<Integer, ActionResponse> response) {
    Map<Integer, String> toReturn = new HashMap<>();
    for(Map.Entry<Integer, ActionResponse> entry : response.entrySet()){
      toReturn.put(entry.getKey(), _gson.toJson(entry.getValue(), ActionResponse.class));
    }
    return toReturn;
  }

  private static class GameState {
    private int playerID;
    private Hand hand;
    private BoardRaw board;
    private int currentTurn;
    private Collection<PublicPlayerRaw> players;

    public GameState(Referee ref, int playerID) {
      this.playerID = playerID;
      this.currentTurn = ref.currentPlayer().getID();
      this.hand = new Hand(ref.getPlayerByID(playerID));
      this.board = new BoardRaw(ref.getBoard());
      this.players = new ArrayList<>();
      for (Player p : ref.getPlayers()) {
        players.add(new PublicPlayerRaw(p, ref.getReadOnlyReferee()));
      }
    }
  }

  public static class CatanSettings {
    private final int numPlayers;
    private final boolean decimal;

    public CatanSettings(int numPlayers, boolean decimal) {
      this.numPlayers = numPlayers;
      this.decimal = decimal;
    }

    public int getNumPlayers() {
      return numPlayers;
    }

    public boolean isDecimal() {
      return decimal;
    }

  }

  private static class Hand {
    private final Map<Resource, Double> resources;
    private final Map<DevelopmentCard, Integer> devCards;

    public Hand(Player player) {
      resources = player.getResources();
      devCards = player.getDevCards();
    }
  }

  private static class BoardRaw {
    private final Collection<TileRaw> tiles;
    private final Collection<IntersectionRaw> intersections;
    private final Collection<PathRaw> paths;

    public BoardRaw(Board board) {
      intersections = new ArrayList<>();
      for (Intersection intersection : board.getIntersections().values()) {
        intersections.add(new IntersectionRaw(intersection));
      }
      paths = new ArrayList<>();
      for (Path path : board.getPaths().values()) {
        paths.add(new PathRaw(path));
      }

      tiles = new ArrayList<>();
      for (Tile tile : board.getTiles()) {
        tiles.add(new TileRaw(tile));
      }
    }
  }

  public static class PathRaw {
    private IntersectionCoordinate start;
    private IntersectionCoordinate end;
    private RoadRaw road;

    public PathRaw(Path path) {
      start = path.getStart().getPosition();
      end = path.getEnd().getPosition();
      road = path.getRoad() != null ? new RoadRaw(path.getRoad()) : null;
    }

  }

  private static class RoadRaw {
    private int player;

    public RoadRaw(Road road) {
      player = road.getPlayer().getID();
    }
  }

  private static class BuildingRaw implements Building {

    private int player;
    private final String type;

    BuildingRaw(Building building) {
      if (building.getPlayer() != null) {
        player = building.getPlayer().getID();
      }
      type = building.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public Map<Integer, Map<Resource, Integer>> collectResource(
        Resource resource) {
      assert false; // Should never be called!
      return null;
    }

    @Override
    public Player getPlayer() {
      assert false; // Should never be called!
      return null;
    }

  }

  private static class IntersectionRaw {

    private final BuildingRaw building;
    private final Port port;
    private final IntersectionCoordinate coordinate;

    IntersectionRaw(Intersection i) {
      building = i.getBuilding() != null ? new BuildingRaw(i.getBuilding())
          : null;
      port = i.getPort();
      coordinate = i.getPosition();
    }

  }

  private static class TileRaw {
    private final HexCoordinate hexCoordinate;
    private final TileType type;
    private final boolean hasRobber;
    private final int number;

    public TileRaw(BoardTile tile) {
      hexCoordinate = tile.getCoordinate();
      type = tile.getType();
      hasRobber = tile.hasRobber();
      number = tile.getRollNumber();
    }
  }

  private static class PublicPlayerRaw {
    private String name;
    private int id;
    private String color;
    private int numSettlements;
    private int numCities;
    private int numPlayedKnights;
    private int numRoads;
    private boolean longestRoad;
    private boolean largestArmy;
    private int victoryPoints;
    private double numResourceCards;
    private int numDevelopmentCards;
    private Map<Resource, Double> rates;

    public PublicPlayerRaw(Player p, Referee r) {
      name = p.getName();
      id = p.getID();
      color = p.getColor();
      numSettlements = p.numSettlements();
      numCities = p.numCities();
      numPlayedKnights = p.numPlayedKnights();
      numRoads = p.numRoads();
      longestRoad = r.hasLongestRoad(p.getID());
      largestArmy = r.hasLargestArmy(p.getID());
      victoryPoints = r.getNumPublicPoints(p.getID());
      rates = r.getBankRates(p.getID());
      numResourceCards = p.getNumResourceCards();
      numDevelopmentCards = p.getNumDevelopmentCards();
    }

  }
}
