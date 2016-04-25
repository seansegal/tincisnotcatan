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

  void addResource(Resource resource, int count);

  void removeResource(Resource resource);

  double getNumResourceCards();

  int getNumDevelopmentCards();

  void addDevelopmentCard(DevelopmentCard card);

  Player getImmutableCopy();

  String getName();

  int getID();

  int numPlayedKnights();

  int numVictoryPoints();

  String getColor();

  boolean canBuyDevelopmentCard();

}
