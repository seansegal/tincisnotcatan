package edu.brown.cs.catan;

import java.util.Collections;
import java.util.Map;

public class HumanPlayer implements Player {

  private Map<Resource, Integer> resources;
  private Map<DevelopmentCard, Integer> devCards;

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
    return Collections.unmodifiableMap(resources);
  }

  @Override
  public Map<DevelopmentCard, Integer> getDevCards() {
    return Collections.unmodifiableMap(devCards);
  }

}
