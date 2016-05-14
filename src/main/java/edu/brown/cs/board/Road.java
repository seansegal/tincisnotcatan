package edu.brown.cs.board;

import edu.brown.cs.catan.Player;

/**
 * Class that is the representation of roads.
 *
 * @author anselvahle
 *
 */
public class Road {
  private Player _player;

  /**
   * Constructor for the class.
   * 
   * @param player
   *          Player who is associated with this road.
   */
  public Road(Player player) {
    _player = player;
  }

  /**
   * Gets the player associated with this road.
   *
   * @return The player whose road this is.
   */
  public Player getPlayer() {
    return _player;
  }

}
