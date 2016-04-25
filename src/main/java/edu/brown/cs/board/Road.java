package edu.brown.cs.board;

import edu.brown.cs.catan.Player;

public class Road {
  private Player _player;

  public Road(Player player) {
    _player = player;
  }

  public Player getPlayer() {
    return _player;
  }

}
