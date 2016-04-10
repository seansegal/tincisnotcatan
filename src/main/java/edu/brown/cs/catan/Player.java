package edu.brown.cs.catan;

import java.util.Map;

public interface Player {

  int numRoads();

  int numSettlements();

  int numCities();

  void buildRoad();

  void buildSettlement();

  void buildCity();

  void buyDevelopmentCard();

  void useRoad();

  void useCity();

  void useSettlement();

  void playDevelopmentCard(DevelopmentCard card);

  Map<Resource, Double> getResources();

  Map<DevelopmentCard, Integer> getDevCards();

  void addResource(Resource resource);

  void removeResource(Resource resource);

  double getNumResourceCards();

  void addDevelopmentCard(DevelopmentCard card);

  Player getImmutableCopy();

}
