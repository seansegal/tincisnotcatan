package edu.brown.cs.catan;

import com.google.gson.JsonObject;

public class GameSettings {

  public final int numPlayers;
  public final int winningPointCount;
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
    int winningPointCount = Settings.WINNING_POINT_COUNT;
    try{
      winningPointCount = settings.get("playTo").getAsInt(); //TODO tell Nick about this
    }
    catch(NullPointerException e){
      //System.out.println("SETTINGS missing numPlayers parameter");
    }
    this.winningPointCount = winningPointCount;
    this.numPlayers = numPlayers;
  }

  // Default Settings
  public GameSettings() {
    this.numPlayers = Settings.DEFAULT_NUM_PLAYERS;
    this.winningPointCount = Settings.WINNING_POINT_COUNT;
  }

}
