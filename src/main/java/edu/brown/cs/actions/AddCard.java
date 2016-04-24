package edu.brown.cs.actions;

import java.util.Map;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;

public class AddCard implements Action {
  private Player _player;
  private Resource _resource;

  public AddCard(Player player, Resource resource) {
    _player = player;
    _resource = resource;
    if (_player == null) {
      String err = String.format("No player sits on this intersection");
      throw new IllegalArgumentException(err);
    }
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    _player.addResource(_resource);
    return null;
  }
}
