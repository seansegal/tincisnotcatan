package edu.brown.cs.networking; // eventually move into catan

import com.google.gson.Gson;

import edu.brown.cs.api.CatanAPI;

// Catan-specific logic for game
// holds reference to GameServer
public final class CatanServer {

  private static final Gson gson = new Gson();

  @Deprecated
  public CatanServer(CatanAPI api) {

  }


}
