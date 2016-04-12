package edu.brown.cs.catan;

import java.util.List;

import edu.brown.cs.board.Board;

public interface Referee {

  void startNextTurn();
  int getTurn();

  boolean devHasBeenPlayed();
  void playDevCard(Player p);
  DevelopmentCard getDevCard();


  Referee getReadOnlyReferee();

  boolean playerHasDiscarded(Player player); //Read
  void playerDiscarded(Player player); //Writes
  void playerMustDiscard(Player player);

  Board getBoard();

  Player getPlayerByID(String id);
  List<Player> getPlayers();


}
