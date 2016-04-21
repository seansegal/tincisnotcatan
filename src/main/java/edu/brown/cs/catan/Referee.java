package edu.brown.cs.catan;

import java.util.Collection;
import java.util.Map;

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

  boolean playerHasDiscarded(int playerID); // Read

  void playerDiscarded(int playerID); // Writes

  void playerMustDiscard(int player);

  Map<Resource, Double> getBankRates(int playerID);

  Board getBoard();

  Player getPlayerByID(int id);

  Collection<Player> getPlayers();

  boolean hasLongestRoad(int playerID);

  boolean hasLargestArmy(int playerID);

  int getNumPublicPoints(int playerID);

  int getNumTotalPoints(int playerID);

}
