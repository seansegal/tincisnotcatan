package edu.brown.cs.board;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;


public class Settlement implements Building {
  private Player _player;

  public Settlement(Player player) {
    _player = player;
  }

  @Override
  public void collectResource(Resource resource) {
    // TODO Call Action AddCard
  }

  @Override
  public Player getPlayer() {
    return _player;
  }

}
