package edu.brown.cs.catan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HumanPlayer implements Player {

  private final Map<Resource, Double> resources;
  private final Map<DevelopmentCard, Integer> devCards; // TODO
  private int numPlayedKnights;
  private int numRoads;
  private int numSettlements;
  private int numCities;
  private final String name;
  private final int id;
  private final String color;

  public HumanPlayer(int id) {
    this.name = ""; //Change to be customizable
    this.id = id;
    this.color = "#000000"; //Custimomize later
    this.numRoads = Settings.INITIAL_ROADS;
    this.numSettlements = Settings.INITIAL_SETTLEMENTS;
    this.numCities = Settings.INITIAL_CITIES;
    // Initialize Resource card hand:
    this.resources = new HashMap<>();
    for (Resource r : Resource.values()) {
      resources.put(r, Settings.INITIAL_RESOURCES);
    }
    // Initialize development card hand:
    this.devCards = new HashMap<>();
    for (DevelopmentCard card : DevelopmentCard.values()) {
      devCards.put(card, 0);
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
    for (Map.Entry<Resource, Double> price : Settings.ROAD_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useRoad();
  }

  @Override
  public void buildSettlement() {
    // Pay for the settlement:
    for (Map.Entry<Resource, Double> price : Settings.SETTLEMENT_COST
        .entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useSettlement();

  }

  @Override
  public void buildCity() {
    // Pay for the city:
    for (Map.Entry<Resource, Double> price : Settings.CITY_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
    useCity();
  }

  @Override
  public void buyDevelopmentCard() {
    // Pay for the development card:
    for (Map.Entry<Resource, Double> price : Settings.DEV_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      assert result >= 0;
      resources.put(price.getKey(), result);
    }
  }

  @Override
  public Map<Resource, Double> getResources() {
    return Collections.unmodifiableMap(resources);
  }

  @Override
  public Map<DevelopmentCard, Integer> getDevCards() {
    return Collections.unmodifiableMap(devCards);
  }

  @Override
  public void addResource(Resource resource) {
    double newCount = resources.get(resource) + 1;
    resources.put(resource, newCount);
  }

  @Override
  public void removeResource(Resource resource) {
    double newCount = resources.get(resource) - 1;
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

  @Override
  public int numPlayedKnights() {
    return numPlayedKnights;
  }

  @Override
  public String toString() {
    return "Player [resources=" + resources + "]";
  }

  @Override
  public void playDevelopmentCard(DevelopmentCard card) {
    int numCards = devCards.get(card);
    if (numCards > 0) {
      devCards.put(card, --numCards);
    } else {
      String message = String.format(
          "The player doesn't have the development card %s", card.toString());
      throw new IllegalArgumentException(message);
    }
  }

  @Override
  public void addDevelopmentCard(DevelopmentCard card) {
    int newVal = devCards.get(card) + 1;
    devCards.put(card, newVal);
  }

  @Override
  public Player getImmutableCopy() {
    return new ReadOnlyPlayer(this);
  }

  @Override
  public double getNumResourceCards() {
    double count = 0;
    for(Map.Entry<Resource, Double> entry : resources.entrySet()){
      count += entry.getValue();
    }
    return count;
  }


  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getID() {
    return id;
  }

  private class ReadOnlyPlayer implements Player {

    private final HumanPlayer _player;

    public ReadOnlyPlayer(HumanPlayer player){
      _player = player;
    }

    @Override
    public int numRoads() {
      return _player.numRoads();
    }

    @Override
    public int numSettlements() {
      return _player.numSettlements();
    }

    @Override
    public int numCities() {
      return _player.numCities();
    }

    @Override
    public void buildRoad() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void buildSettlement() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void buildCity() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void buyDevelopmentCard() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void useRoad() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void useCity() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void useSettlement() {
      throw new UnsupportedOperationException("Player is immutable and cannot build.");
    }

    @Override
    public void playDevelopmentCard(DevelopmentCard card) {
      throw new UnsupportedOperationException("Player is immutable and cannot use development card.");
    }

    @Override
    public Map<Resource, Double> getResources() {
      return _player.getResources();
    }

    @Override
    public Map<DevelopmentCard, Integer> getDevCards() {
      return _player.getDevCards();
    }

    @Override
    public void addResource(Resource resource) {
      throw new UnsupportedOperationException("Player is immutable and cannot add resource cards.");
    }

    @Override
    public void removeResource(Resource resource) {
      throw new UnsupportedOperationException("Player is immutable and cannot remove resource cards.");
    }

    @Override
    public void addDevelopmentCard(DevelopmentCard card) {
      throw new UnsupportedOperationException("Player is immutable and cannot add development cards.");

    }

    @Override
    public Player getImmutableCopy() {
      return this;
    }

    @Override
    public double getNumResourceCards() {
      return _player.getNumResourceCards();
    }

    @Override
    public String getName() {
      return _player.getName();
    }

    @Override
    public int getID() {
      return _player.getID();
    }

    @Override
    public int numPlayedKnights() {
      return _player.numPlayedKnights();
    }

  }

}
