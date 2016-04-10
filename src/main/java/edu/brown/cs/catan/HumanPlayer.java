package edu.brown.cs.catan;

import java.util.Collections;
import java.util.Map;

public class HumanPlayer implements Player {

  private Map<Resource, Integer> resources;
  private Map<DevelopmentCard, Integer> devCards; // TODO
  private int numRoads;
  private int numSettlements;
  private int numCities;

  public HumanPlayer() {
    this.numRoads = Settings.INITIAL_ROADS;
    this.numSettlements = Settings.INITIAL_SETTLEMENTS;
    this.numCities = Settings.INITIAL_CITIES;
    // Setting initial resources:
    for (Resource r : Resource.values()) {
      resources.put(r, Settings.INITIAL_RESOURCES);
    }
  }

  @Override
  public int numRoads() {
    return numRoads;
  }

  @Override
  public int numSettlements() {
    return numSettlements;
  }

  @Override
  public int numCities() {
    return numCities;
  }

  @Override
  public void buildRoad() {
    // Pay for the road:
    for (Map.Entry<Resource, Integer> price : Settings.ROAD_COST.entrySet()) {
      int result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useRoad();
  }

  @Override
  public void buildSettlement() {
    // Pay for the settlement:
    for (Map.Entry<Resource, Integer> price : Settings.SETTLEMENT_COST
        .entrySet()) {
      int result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useSettlement();

  }

  @Override
  public void buildCity() {
    // Pay for the city:
    for (Map.Entry<Resource, Integer> price : Settings.CITY_COST.entrySet()) {
      int result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useCity();
  }

  @Override
  public Map<Resource, Integer> getResources() {
    return Collections.unmodifiableMap(resources);
  }

  @Override
  public Map<DevelopmentCard, Integer> getDevCards() {
    return Collections.unmodifiableMap(devCards);
  }

  @Override
  public void addResource(Resource resource) {
    int newCount = resources.get(resource) + 1;
    resources.put(resource, newCount);
  }

  @Override
  public void removeResource(Resource resource) {
    int newCount = resources.get(resource) - 1;
    assert newCount >= 0;
    resources.put(resource, newCount);
  }

  @Override
  public void useRoad() {
    assert numRoads > 0;
    numRoads--;
  }

  @Override
  public void useCity() {
    assert numCities > 0;
    numCities--;

  }

  @Override
  public void useSettlement() {
    assert numSettlements > 0;
    numSettlements--;
  }

  // Play development card?
  // Buy development card?

}
