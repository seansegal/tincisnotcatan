package edu.brown.cs.catan;

public enum Resource {

  WHEAT("wheat"), SHEEP("sheep"), ORE("ore"), WOOD("wood"), BRICK("brick"), WILDCARD(
      "of anything");
  
  private final String _description;

  private Resource(String description) {
    _description = description;
  }

  @Override
  public String toString() {
    return _description;
  }

}
