package edu.brown.cs.networking; // eventually move into catan

import edu.brown.cs.api.CatanAPI;

// Catan-specific logic for game
// holds reference to GameServer
public final class CatanServer {

  public CatanServer(CatanAPI api) {
    GameServer.getInstance().launch((req) -> {
      switch(req) {
        case "getBoard" :
          return api.getBoard();
        case "getPlayers" :
          return api.getPlayers();
        default :
          throw new IllegalArgumentException("Unsupported action : " + req);
      }
    });
  }


}
