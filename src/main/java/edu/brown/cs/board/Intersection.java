package edu.brown.cs.board;

import java.nio.file.Path;

import edu.brown.cs.catan.Player;
import edu.brown.cs.graph.Node;

public interface Intersection extends Node<Path, Intersection> {

  // int intersectionID;
  // Port port;

  int getID();

  void notifyBuilding();

  void placeSettlement(Player p);

  void placeCity(Player p);

  boolean canPlaceSettlement(Player p);

  boolean canPlaceCity(Player p);

}
