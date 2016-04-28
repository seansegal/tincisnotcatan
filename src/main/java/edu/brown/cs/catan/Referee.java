package edu.brown.cs.catan;

import java.util.Collection;
import java.util.Map;

import edu.brown.cs.actions.FollowUpAction;
import edu.brown.cs.board.Board;

public interface Referee {

  void startNextTurn();

  Turn getTurn();

  Player currentPlayer();

  void playDevCard();

  boolean gameIsFull();

  int addPlayer(String name);

  int addPlayer(String name, String color);

  DevelopmentCard getDevCard();

  GameSettings getGameSettings();

  Referee getReadOnlyReferee();

  Map<Resource, Double> getBankRates(int playerID);

  Board getBoard();

  Player getPlayerByID(int id);

  Collection<Player> getPlayers();

  boolean hasLongestRoad(int playerID);

  boolean hasLargestArmy(int playerID);

  int getNumPublicPoints(int playerID);

  int getNumTotalPoints(int playerID);

  Bank getBank();

  boolean isSetUp();

  FollowUpAction getNextFollowUp(int playerID);

  void addFollowUp(Collection<FollowUpAction> actions);

  public void removeFollowUp(FollowUpAction action);
}
