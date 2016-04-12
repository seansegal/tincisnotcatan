package edu.brown.cs.catan;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class Settings {

  // Number of pieces each player begins the game with:
  public final static int INITIAL_ROADS = 15;
  public final static int INITIAL_SETTLEMENTS = 6;
  public final static int INITIAL_CITIES = 4;

  // Number of each tile type on the board:
  public final static int NUM_WOOD_TILE = 0;
  public final static int NUM_BRICK_TILE = 0;
  public final static int NUM_WHEAT_TILE = 0;
  public final static int NUM_SHEEP_TILE = 0;
  public final static int NUM_ORE_TILE = 0;
  public final static int NUM_DESERT_TILE = 0;

  // How many resources should players start with:
  public final static double INITIAL_RESOURCES = 0.0;

  // Building costs:
  public final static Map<Resource, Double> CITY_COST = ImmutableMap.of(
      Resource.WHEAT, 2.0, Resource.ORE, 3.0);
  public final static Map<Resource, Double> ROAD_COST = ImmutableMap.of(
      Resource.BRICK, 1.0, Resource.WOOD, 1.0);
  public final static Map<Resource, Double> DEV_COST = ImmutableMap.of(
      Resource.WHEAT, 1.0, Resource.SHEEP, 1.0, Resource.ORE, 1.0);
  public final static Map<Resource, Double> SETTLEMENT_COST = ImmutableMap
      .of(Resource.BRICK, 1.0, Resource.SHEEP, 1.0, Resource.WHEAT, 1.0,
          Resource.WOOD, 1.0);

  // Bank rate for trading in basic game:
  public final static double BANK_RATE = 4.00;

  //Development Card Deck:
  public final static int NUM_KNIGHTS = 14;
  public final static int NUM_POINTS = 5;
  public final static int NUM_YOP = 2;
  public final static int NUM_MONOPOLY = 2;
  public final static int NUM_ROADBUILDING = 2;

}
