package edu.brown.cs.catan;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class Settings {

  public final static int INITIAL_ROADS = 15;
  public final static int INITIAL_SETTLEMENTS = 6;
  public final static int INITIAL_CITIES = 4;

  // How many resources should players start with:
  public final static int INITIAL_RESOURCES = 0;

  // Building costs:
  public final static Map<Resource, Integer> CITY_COST = ImmutableMap.of(
      Resource.WHEAT, 2, Resource.ORE, 3);
  public final static Map<Resource, Integer> ROAD_COST = ImmutableMap.of(
      Resource.BRICK, 1, Resource.WOOD, 1);
  public final static Map<Resource, Integer> DEV_COST = ImmutableMap.of(
      Resource.WHEAT, 1, Resource.SHEEP, 1, Resource.ORE, 1);
  public final static Map<Resource, Integer> SETTLEMENT_COST = ImmutableMap
      .of(Resource.BRICK, 1, Resource.SHEEP, 1, Resource.WHEAT, 1,
          Resource.WOOD, 1);

}
