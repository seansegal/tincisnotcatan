package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import edu.brown.cs.board.Board;
import edu.brown.cs.board.BoardTile;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.Path;
import edu.brown.cs.board.Tile;
import edu.brown.cs.board.TileType;
import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class CatanConverter {

  private Gson _gson;


  public CatanConverter() {
    _gson = new Gson();
  }

  public String getBoard(Referee referee) {
    return _gson.toJson(new BoardRaw(referee.getBoard()));
  }

  public String getHand(int playerID, Referee referee){
    return _gson.toJson(new Hand(referee.getPlayerByID(playerID)));
  }

  public String getPlayers(Referee referee) {
    List<PublicPlayerRaw> players = new ArrayList<>();
    for (Player p : referee.getPlayers()) {
      players.add(new PublicPlayerRaw(p, referee.getReadOnlyReferee()));
    }
    return _gson.toJson(players);
  }

  public static class Hand {
    private final Map<Resource, Double> resources;
    private final Map<DevelopmentCard, Integer> devCards;

    public Hand(Player player){
      resources = player.getResources();
      devCards = player.getDevCards();
    }
  }


  private static class BoardRaw {
    private final Collection<TileRaw> tiles;
    private final Collection<Intersection> intersections;
    private final Collection<Path> paths;

    public BoardRaw(Board board) {
      intersections = board.getIntersections().values();
      paths = board.getPaths().values();
      tiles = new ArrayList<>();
      for (Tile tile : board.getTiles()) {
        tiles.add(new TileRaw(tile));
      }
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
    }

    @Override
    public String toString() {
      return "PublicPlayerRaw [name=" + name + ", id=" + id + ", color="
          + color + ", numSettlements=" + numSettlements + ", numCities="
          + numCities + ", numPlayedKnights=" + numPlayedKnights
          + ", numRoads=" + numRoads + ", longestRoad=" + longestRoad
          + ", largestArmy=" + largestArmy + ", victoryPoints=" + victoryPoints
          + ", rates=" + rates + "]";
    }

  }

}
