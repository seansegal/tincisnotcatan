package edu.brown.cs.catan;

import com.google.gson.Gson;

public class CatanAPI {

  public static void main(String[] args) {
    System.out.println(new CatanAPI().getPlayers());
  }

  private Referee _referee;
  private Gson _gson;

  public CatanAPI() {
    // How are we setting up the players here?
    _referee = new MasterReferee();
    _gson = new Gson();
  }

  public String getBoard() {
    // TODO
    return null;
  }

  public String getHand() {
    // TODO
    return null;
  }

  public String getPlayers() {
    return _gson.toJson(_referee.getPlayers());
  }

  private static class PublicPlayerRaw {
    private String name; //
    private int id; //
    private String color; //
    private int numSettlements;
    private int numCities;
    private int numPlayedKnights;
    private int numRoads;
    private boolean longestRoad;
    private boolean largestArmy;
    private int victoryPoints;

    public PublicPlayerRaw(Player p){
      numCities = p.numCities();
      numSettlements = p.numSettlements();
      numRoads = p.numRoads();
      name = p.getName();
      id = p.getID();
      numPlayedKnights = p.numPlayedKnights();
    }
  }

}
