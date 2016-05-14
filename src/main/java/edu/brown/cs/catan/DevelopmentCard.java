package edu.brown.cs.catan;

/**
 * Represents a Catan DevelopmentCard. This Enum currently contains the Standard
 * Development Cards from the original game of Settlers of Catan. New
 * development cards could be added here in the future.
 *
 */
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

  /**
   * Used for sending messages to Players.
   *
   * @return A String in a helpful format for Player messages.
   */
  public String toFancyString() {
    return this.description;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
