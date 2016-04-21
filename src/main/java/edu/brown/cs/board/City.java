package edu.brown.cs.board;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;



public class City implements Building{
  private Player _player;

  public City(Player player) {
    _player = player;
  }

  @Override
  public void collectResource(Resource resource) {
    // TODO Add Card Action
  }

  @Override
  public Player getPlayer() {
    return _player;
  }

}
