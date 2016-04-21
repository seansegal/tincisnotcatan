package edu.brown.cs.catan;

import java.util.Collection;

import edu.brown.cs.board.Board;

public interface Referee {

  void startNextTurn();

  int getTurn();

  Player currentPlayer();

  // Development Card
  boolean devHasBeenPlayed();

  void playDevCard();

  DevelopmentCard getDevCard();

  Referee getReadOnlyReferee();

  boolean playerHasDiscarded(Player player); // Read

  void playerDiscarded(Player player); // Writes

  void playerMustDiscard(Player player);

  Board getBoard();

  Player getPlayerByID(int id);

  Collection<Player> getPlayers();

  boolean hasLongestRoad(Player player);

  boolean hasLargestArmy(Player player);

  int getNumPublicPoints(Player player);

  int getNumTotalPoints(Player player);

}
