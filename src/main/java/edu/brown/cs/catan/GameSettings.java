package edu.brown.cs.catan;

public class GameSettings {

  public final int NUM_PLAYERS;
  public final String[] COLORS = {"#BF2720", "#115EC9", "#DFA629", "#EDEAD9"};

  public GameSettings(int numPlayers){
    this.NUM_PLAYERS = numPlayers;
  }

  //Default Settings
  public GameSettings(){
    this.NUM_PLAYERS = Settings.DEFAULT_NUM_PLAYERS;
  }



}
