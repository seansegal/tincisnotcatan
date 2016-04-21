package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class CatanAPI {

  public static void main(String[] args) {
    System.out.println(new CatanAPI().getBoard());
  }

  private Referee _referee;
  private Gson _gson;

  public CatanAPI() {
    // TODO: How are we setting up the players here?
    _referee = new MasterReferee();
    _gson = new Gson();
  }

  public String getBoard() {
    return _gson.toJson(_referee.getBoard());
  }

  public String getHand(int playerID) {
    // TODO
    return null;
  }

  public String getPlayers() {
    List<PublicPlayerRaw> players = new ArrayList<>();
    for (Player p : _referee.getPlayers()) {
      players.add(new PublicPlayerRaw(p, _referee.getReadOnlyReferee()));
    }
    return _gson.toJson(players);
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
