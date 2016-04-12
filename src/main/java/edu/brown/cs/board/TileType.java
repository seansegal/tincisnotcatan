package edu.brown.cs.board;

import edu.brown.cs.catan.Resource;

public enum TileType {

  WHEAT(Resource.WHEAT), SHEEP(Resource.SHEEP), ORE(Resource.ORE), WOOD(
      Resource.WOOD), BRICK(Resource.BRICK), DESERT(null);

  private Resource _resType;

  private TileType(Resource res) {
    _resType = res;
  }

  public Resource getType() {
    return _resType;
  }

}
