package edu.brown.cs.catan;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class Settings {

  // Number of pieces each player begins the game with:
  public final static int INITIAL_ROADS = 15;
  public final static int INITIAL_SETTLEMENTS = 6;
  public final static int INITIAL_CITIES = 4;

  // Numbers on the board:
  public final static int[] ROLL_NUMS = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8,
      10, 9, 4, 5, 6, 3, 10, 11 };

  // Number of each tile type on the board:
  public final static int NUM_WOOD_TILE = 3;
  public final static int NUM_BRICK_TILE = 3;
  public final static int NUM_WHEAT_TILE = 4;
  public final static int NUM_SHEEP_TILE = 4;
  public final static int NUM_ORE_TILE = 4;
  public final static int NUM_DESERT_TILE = 1;

  // How many resources should players start with (Beware of changing this.
  // Wildcards!):
  public final static double INITIAL_RESOURCES = 0.0;

  // Building costs:
  public final static Map<Resource, Double> CITY_COST = ImmutableMap.of(
      Resource.WHEAT, 2.0, Resource.ORE, 3.0);
  public final static Map<Resource, Double> ROAD_COST = ImmutableMap.of(
      Resource.BRICK, 1.0, Resource.WOOD, 1.0);
  public final static Map<Resource, Double> DEV_COST = ImmutableMap.of(
      Resource.WHEAT, 1.0, Resource.SHEEP, 1.0, Resource.ORE, 1.0);
  public final static Map<Resource, Double> SETTLEMENT_COST = ImmutableMap.of(
      Resource.BRICK, 1.0, Resource.SHEEP, 1.0, Resource.WHEAT, 1.0,
      Resource.WOOD, 1.0);

  // Bank rates for trading in basic game:
  public final static double BANK_RATE = 4.0;
  public final static Map<Resource, Double> PORT_RATES = new ImmutableMap.Builder<Resource, Double>()
      .put(Resource.BRICK, 2.0)
      .put(Resource.ORE, 2.0)
      .put(Resource.SHEEP, 2.0)
      .put(Resource.WHEAT, 2.0)
      .put(Resource.WOOD, 2.0)
      .put(Resource.WILDCARD, 3.0)
      .build();

  // Development Card Deck:
  public final static int NUM_KNIGHTS = 14;
  public final static int NUM_POINTS = 5;
  public final static int NUM_YOP = 2;
  public final static int NUM_MONOPOLY = 2;
  public final static int NUM_ROADBUILDING = 2;

  // Default number of players
  public static final int DEFAULT_NUM_PLAYERS = 4;

  // Largest Army & Longest Road Threshold
  public static final int LARGEST_ARMY_THRESH = 3;
  public static final int LONGEST_ROAD_THRESH = 5;

  // Point values
  public static final int SETTLEMENT_POINT_VAL = 1;
  public static final int CITY_POINT_VAL = 2;
  public static final int LONGEST_ROAD_POINT_VAL = 2;
  public static final int LARGEST_ARMY_POINT_VAL = 2;

}
