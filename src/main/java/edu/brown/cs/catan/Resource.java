package edu.brown.cs.catan;

public enum Resource {

  WHEAT("wheat", "a wheat"), SHEEP("sheep", "a sheep"), ORE("ore", "an ore"), WOOD(
      "wood", "a wood"), BRICK("brick", "a brick"), WILDCARD("wildcard", "any card");

  private final String _description;
  private final String _withArticle;

  private Resource(String description, String withArticle) {
    _description = description;
    _withArticle = withArticle;
  }

  @Override
  public String toString() {
    return _description;
  }

  public String stringWithArticle() {
    return _withArticle;
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
