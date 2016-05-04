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
  private int _numVictoryPoints;

  public HumanPlayer(int id, String name, String color) {
    this.name = name;
    this.id = id;
    this.color = color;
    this.numRoads = Settings.INITIAL_ROADS;
    this.numSettlements = Settings.INITIAL_SETTLEMENTS;
    this.numCities = Settings.INITIAL_CITIES;
    _numVictoryPoints = 0;
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
  public String getColor() {
    return color;
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
  }

  @Override
  public boolean canBuildRoad() {
    // Check in the player can pay for the road:
    for (Map.Entry<Resource, Double> price : Settings.ROAD_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      if (result < 0) {
        return false;
      }
    }
    if (numRoads > 0) {
      return true;
    }
    return false;
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
  }

  @Override
  public boolean canBuildSettlement() {
    // Pay for the settlement:
    for (Map.Entry<Resource, Double> price : Settings.SETTLEMENT_COST
        .entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      if (result < 0) {
        return false;
      }
    }
    if (numSettlements > 0) {
      return true;
    }
    return false;
  }

  @Override
  public void buildCity() {
    if (canBuildCity()) {
      for (Map.Entry<Resource, Double> price : Settings.CITY_COST.entrySet()) {
        double result = resources.get(price.getKey()) - price.getValue();
        assert result >= 0;
        resources.put(price.getKey(), result);
      }
    }
  }

  @Override
  public boolean canBuildCity() {
    // Pay for the city:
    for (Map.Entry<Resource, Double> price : Settings.CITY_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      if (result < 0) {
        return false;
      }
    }
    if (numCities > 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean canBuyDevelopmentCard() {
    // Pay for the development card:
    for (Map.Entry<Resource, Double> price : Settings.DEV_COST.entrySet()) {
      double result = resources.get(price.getKey()) - price.getValue();
      if (result < 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void buyDevelopmentCard() {
    if (canBuyDevelopmentCard()) {
      for (Map.Entry<Resource, Double> price : Settings.DEV_COST.entrySet()) {
        double result = resources.get(price.getKey()) - price.getValue();
        assert result >= 0;
        resources.put(price.getKey(), result);
      }
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
    resources.replace(resource, resources.get(resource) + 1.0);
  }

  @Override
  public void addResource(Resource resource, double count) {
    resources.replace(resource, resources.get(resource) + count);
  }

  @Override
  public boolean hasResource(Resource res, double count) {
    return resources.get(res) >= count;
  }

  @Override
  public void removeResource(Resource resource) {
    double newCount = resources.get(resource) - 1.0;
    assert newCount >= 0.0;
    resources.put(resource, newCount);
  }

  @Override
  public void removeResource(Resource resource, double count) {
    double newCount = resources.get(resource) - count;
    assert newCount >= 0;
    resources.put(resource, newCount);
  }

  @Override
  public void addResource(Resource resource, double count, Bank bank) {
    bank.getResource(resource, count);
    addResource(resource, count);
  }

  @Override
  public void removeResource(Resource resource, double count, Bank bank) {
    bank.discardResource(resource, count);
    removeResource(resource, count);

  }

  @Override
  public void useRoad() {
    assert numRoads > 0;
    numRoads--;
  }

  @Override
  public void useCity() {
    assert numCities > 0;
    assert numSettlements != Settings.INITIAL_SETTLEMENTS;
    numCities--;
    numSettlements++;
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
      if (card == DevelopmentCard.KNIGHT) {
        numPlayedKnights++;
      }
      devCards.put(card, --numCards);
    } else {
      String message = String.format(
          "The player doesn't have the development card %s", card.toString());
      throw new IllegalArgumentException(message);
    }
  }

  @Override
  public void addDevelopmentCard(DevelopmentCard card) {
    if (card == DevelopmentCard.POINT) {
      _numVictoryPoints++;
    }
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
    for (Map.Entry<Resource, Double> entry : resources.entrySet()) {
      count += entry.getValue();
    }
    return count;
  }

  @Override
  public int getNumDevelopmentCards() {
    int count = 0;
    for (Integer num : devCards.values()) {
      count += num;
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

  @Override
  public int numVictoryPoints() {
    return _numVictoryPoints;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Player)) {
      return false;
    }
    Player other = (Player) obj;
    if (id != other.getID()) {
      return false;
    }
    return true;
  }

  private class ReadOnlyPlayer implements Player {

    private final HumanPlayer _player;

    public ReadOnlyPlayer(HumanPlayer player) {
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
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void buildSettlement() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void buildCity() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void buyDevelopmentCard() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void useRoad() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void useCity() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void useSettlement() {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot build.");
    }

    @Override
    public void playDevelopmentCard(DevelopmentCard card) {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot use development card.");
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
      throw new UnsupportedOperationException(
          "Player is immutable and cannot add resource cards.");
    }

    @Override
    public void removeResource(Resource resource) {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot remove resource cards.");
    }

    @Override
    public void addDevelopmentCard(DevelopmentCard card) {
      throw new UnsupportedOperationException(
          "Player is immutable and cannot add development cards.");

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

    @Override
    public String getColor() {
      return _player.getColor();
    }

    @Override
    public int getNumDevelopmentCards() {
      return _player.getNumDevelopmentCards();
    }

    @Override
    public int numVictoryPoints() {
      return _player.numVictoryPoints();
    }

    @Override
    public void addResource(Resource resource, double count) {
      throw new UnsupportedOperationException(
          "A ReadOnlyPlayer cannot add resource cards.");
    }

    @Override
    public boolean canBuyDevelopmentCard() {
      return _player.canBuyDevelopmentCard();
    }

    @Override
    public boolean canBuildRoad() {
      return _player.canBuildRoad();
    }

    @Override
    public void removeResource(Resource resource, double count) {
      throw new UnsupportedOperationException(
          "A ReadOnlyPlayer cannot remove resource cards.");
    }

    @Override
    public boolean canBuildCity() {
      return _player.canBuildCity();
    }

    @Override
    public boolean canBuildSettlement() {
      return _player.canBuildSettlement();
    }

    @Override
    public int hashCode() {
      return _player.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Player)) {
        return false;
      }
      Player other = (Player) obj;
      if (_player == null) {
        if (other != null) {
          return false;
        }
      } else if (!this.equals(other)) {
        return false;
      }
      return true;
    }

    @Override
    public boolean hasResource(Resource res, double count) {
      return _player.hasResource(res, count);
    }

    @Override
    public void addResource(Resource resource, double count, Bank bank) {
      throw new UnsupportedOperationException(
          "A ReadOnlyPlayer cannot add resource cards.");

    }

    @Override
    public void removeResource(Resource resource, double count, Bank bank) {
      throw new UnsupportedOperationException(
          "A ReadOnlyPlayer cannot remove resource cards.");

    }

  }

}
