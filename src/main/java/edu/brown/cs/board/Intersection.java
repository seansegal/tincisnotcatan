package edu.brown.cs.board;

import edu.brown.cs.catan.Player;

public interface Intersection {

  //instance variable: ID

  int getID();
  void notifyBuilding();
  void placeSettlement(Player p);
  void placeCity(Player p);
  boolean canPlaceSettlement(Player p);
  boolean canPlaceCity(Player p);


}
