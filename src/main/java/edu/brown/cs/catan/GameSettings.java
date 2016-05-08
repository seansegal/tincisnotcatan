package edu.brown.cs.catan;

import com.google.gson.JsonObject;

public class GameSettings {

  public final int numPlayers;
  public final int winningPointCount;
  public final String[] COLORS = { "#BF2720", "#115EC9", "#DFA629", "#EDEAD9" };
  public final boolean isDecimal;
  public final boolean isDynamic;

  public GameSettings(JsonObject settings) {
    int numPlayers = Settings.DEFAULT_NUM_PLAYERS;
    try {
      numPlayers = settings.get("numPlayers").getAsInt();
    } catch (NullPointerException e) {
      System.out.println("SETTINGS missing numPlayers parameter");
    }
    int winningPointCount = Settings.WINNING_POINT_COUNT;
    try {
      winningPointCount = settings.get("victoryPoints").getAsInt();
    } catch (NullPointerException e) {
      System.out.println("SETTINGS missing victoryPoints parameter");
    }
    boolean isDecimal = false;
    try {
      isDecimal = settings.get("isDecimal").getAsBoolean();
    } catch (NullPointerException e) {
      System.out.println("SETTINGS missing isDecimal parameter");
    }
    boolean isDynamic = false;
    try {
      isDynamic = settings.get("isDynamic").getAsBoolean();
    } catch (NullPointerException e) {
      System.out.println("SETTINGS missing isDynamic parameter");
    }
    this.winningPointCount = winningPointCount;
    this.numPlayers = numPlayers;
    this.isDecimal = isDecimal;
    this.isDynamic = isDynamic;
  }

  // Default Settings
  public GameSettings() {
    this.numPlayers = Settings.DEFAULT_NUM_PLAYERS;
    this.winningPointCount = Settings.WINNING_POINT_COUNT;
    this.isDecimal = false;
    this.isDynamic = false;
  }

}
