package edu.brown.cs.networking;

import org.json.JSONObject;

import edu.brown.cs.catan.Player;

public class AttentivePlayer implements Attentive {

  private Player internalPlayer;

  // should it take a Player object? probably.
  public AttentivePlayer() {

  }

  @Override
  public void handleWebRequest(JSONObject j) {
    // TODO Auto-generated method stub
    // parse json,

  }

  // pass all of these to internal player.

}
