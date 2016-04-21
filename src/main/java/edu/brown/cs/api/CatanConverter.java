package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import edu.brown.cs.board.BoardTile;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.Path;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class CatanConverter {


  private Gson _gson;

  public CatanConverter(){
    _gson = new Gson();
  }

  public String getPlayers(Referee referee) {
    List<PublicPlayerRaw> players = new ArrayList<>();
    for (Player p : referee.getPlayers()) {
      players.add(new PublicPlayerRaw(p, referee.getReadOnlyReferee()));
    }
    return _gson.toJson(players);
  }



  private static class BoardRaw {
    private Collection<TileRaw> tiles;
    private Collection<Intersection> intersections;
    private Collection<Path> paths;
  }

  private static class TileRaw {
    private HexCoordinate hexCoordinate;
    private Resource type;
    private boolean hasRobber;
    private int number;

    public TileRaw(BoardTile tile){

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
      longestRoad = r.hasLongestRoad(p);
      largestArmy = r.hasLargestArmy(p);
      victoryPoints = r.getNumPublicPoints(p);
      rates = r.getBankRates(p);
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
