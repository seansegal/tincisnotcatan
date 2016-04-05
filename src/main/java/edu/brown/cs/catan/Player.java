package edu.brown.cs.catan;

import java.util.Map;

public interface Player {

  int numRoads();
  int numSettlements();
  int numCities();

  void buildRoad();
  void buildSettlement();
  void buildCity();

  Map<Resource, Integer> getResources();
  Map<DevelopmentCard, Integer> getDevCards();


}
