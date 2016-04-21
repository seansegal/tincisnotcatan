package edu.brown.cs.networking;

import java.util.Map;

import org.json.JSONObject;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;


public class AttentivePlayer implements Attentive, Player {

  private Player internalPlayer;


  // should it take a Player object? probably.
  public AttentivePlayer() {

  }


  @Override
  public void handleWebRequest(JSONObject j) {
    // TODO Auto-generated method stub
    // parse json,

  }

  // pass all of these to internal player.

  @Override
  public int numRoads() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public int numSettlements() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public int numCities() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public void buildRoad() {
    // TODO Auto-generated method stub

  }


  @Override
  public void buildSettlement() {
    // TODO Auto-generated method stub

  }


  @Override
  public void buildCity() {
    // TODO Auto-generated method stub

  }


  @Override
  public Map<Resource, Integer> getResources() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Map<DevelopmentCard, Integer> getDevCards() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void addResource(Resource resource) {
    // TODO Auto-generated method stub

  }

}
