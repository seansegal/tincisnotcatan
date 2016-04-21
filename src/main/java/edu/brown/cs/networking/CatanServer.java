package edu.brown.cs.networking; // eventually move into catan

import java.util.Collections;
import java.util.List;

import edu.brown.cs.catan.Player;

// Catan-specific logic for game
// holds reference to GameServer
public final class CatanServer {


  public static void main(String[] args) {
    GameServer.getInstance().launch();
  }


  public List<Player> getPlayers() {
    return Collections.emptyList();
  }
}
