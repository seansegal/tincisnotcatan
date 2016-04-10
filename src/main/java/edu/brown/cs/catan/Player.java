package edu.brown.cs.catan;

import java.util.Map;

public interface Player {

  int numRoads();

  int numSettlements();

  int numCities();

  void buildRoad();

  void buildSettlement();

  void buildCity();

  void useRoad();

  void useCity();

  void useSettlement();

  Map<Resource, Integer> getResources();

  Map<DevelopmentCard, Integer> getDevCards();

  void addResource(Resource resource);

  void removeResource(Resource resource);

}
