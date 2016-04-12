package edu.brown.cs.catan;

import java.util.List;

import edu.brown.cs.board.Board;

public interface Referee {

  void startNextTurn();

  boolean devHasBeenPlayed();

  DevelopmentCard getDevCard();

  Referee getReadOnlyReferee();

  int getTurn();

  boolean playerHasDiscarded(Player player);

  Board getBoard();

  Player getPlayerByID(String id);

  List<Player> getPlayers();


}
