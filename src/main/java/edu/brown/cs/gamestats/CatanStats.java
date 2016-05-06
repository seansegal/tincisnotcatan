package edu.brown.cs.gamestats;

//Higher level class for storing GameStats in database
public class CatanStats {

  private static int startedGames = 0;
  private static int finishedGames = 0;

  public synchronized static GameStats getGameStatsObject(){
    startedGames++;
    return new GameStats();
  }

  public synchronized static void processGameStats(GameStats stats){
    finishedGames++;
    //TODO: process game stats, store to database...etc
  }



}
