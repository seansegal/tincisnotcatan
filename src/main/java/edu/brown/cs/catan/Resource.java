package edu.brown.cs.catan;

public enum Resource {

  WHEAT("wheat"), SHEEP("sheep"), ORE("ore"), WOOD("wood"), BRICK("brick"), WILDCARD(
      "wildcard");

  private final String _description;

  private Resource(String description) {
    _description = description;
  }

  @Override
  public String toString() {
    return _description;
  }

  public static Resource stringToResource(String str) {
    switch (str) {
    case "wheat":
      return WHEAT;
    case "sheep":
      return SHEEP;
    case "ore":
      return ORE;
    case "wood":
      return WOOD;
    case "brick":
      return BRICK;
    case "wildcard":
      return WILDCARD;
    default:
      throw new IllegalArgumentException(String.format(
          "The resource %s does not exist.", str));
    }
  }
}
