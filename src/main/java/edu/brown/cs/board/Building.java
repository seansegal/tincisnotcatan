package edu.brown.cs.board;

import java.util.Map;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public interface Building {

  Map<Integer, Map<Resource, Integer>> collectResource(Resource resource);

  Player getPlayer();

}
