package edu.brown.cs.catan;

public enum DevelopmentCard {

  KNIGHT("Knight", "a Knight"), YEAR_OF_PLENTY("Year of Plenty",
      "Year of Plenty"), MONOPOLY("Monopoly", "Monopoly"), POINT(
      "Victory Point", "a Victory Point."), ROAD_BUILDING("Road Building",
      "Road Building");

  private final String name;
  private final String description;

  private DevelopmentCard(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String toFancyString() {
    return this.description;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
