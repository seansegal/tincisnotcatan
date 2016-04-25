package edu.brown.cs.board;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class Settlement implements Building {
  private Player _player;

  public Settlement(Player player) {
    _player = player;
  }

  @Override
  public Map<Integer, Map<Resource, Integer>> collectResource(Resource resource) {
<<<<<<< HEAD
=======
    Action addCard = new AddCard(_player, resource);
    addCard.execute(); //TODO: change and put in RollDice
>>>>>>> 39761352966c7526df05832110bc8f6bcd4d1b40
    Map<Resource, Integer> resourceCount = new HashMap<Resource, Integer>();
    resourceCount.put(resource, 1);
    Map<Integer, Map<Resource, Integer>> playerResource = new HashMap<Integer, Map<Resource, Integer>>();
    playerResource.put(_player.getID(), resourceCount);
    return playerResource;
  }

  @Override
  public Player getPlayer() {
    return _player;
  }

}
