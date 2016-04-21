package edu.brown.cs.catan;

public enum DevelopmentCard {

  KNIGHT( "Knight", "Move the robber. Steal 1 resource card from the owner of an adjacent settlement or city."),
  YEAR_OF_PLENTY( "Year of Plenty", "Take any 2 resource cards from the band and add them to your hand."),
  MONOPOLY( "Monopoly", "When you play this card, announce one type of resource. All other players must give you all their resource cards of that type."),
  POINT( "Victory Point", "1 Victory Point!"),
  ROAD_BUILDING("Road Building", "Build 2 new roads.");

  private final String name;
  private final String description;

  private DevelopmentCard(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
