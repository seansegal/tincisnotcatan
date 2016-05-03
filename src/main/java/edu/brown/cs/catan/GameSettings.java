package edu.brown.cs.catan;

import com.google.gson.JsonObject;

public class GameSettings {

  public final int NUM_PLAYERS;
  public final String[] COLORS = { "#BF2720", "#115EC9", "#DFA629", "#EDEAD9" };
  public final boolean isDecimal = false;

  public GameSettings(JsonObject settings) {
    int numPlayers = Settings.DEFAULT_NUM_PLAYERS;;
    try{
      numPlayers = settings.get("numPlayers").getAsInt();
    }
    catch(NullPointerException e){
      System.out.println("SETTINGS missing numPlayers parameter");
    }
    this.NUM_PLAYERS = numPlayers;
  }

  // Default Settings
  public GameSettings() {
    this.NUM_PLAYERS = Settings.DEFAULT_NUM_PLAYERS;
  }

}
