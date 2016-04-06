package edu.brown.cs.catan;

public interface Referee {

  // Board
  // devCardHasBeenPlayed
  // turnOrder - List<Player>
  // Players
  // Bank
  // DevCardDeck - returns Enum
  // Set<Player> hasDiscarded

  /**
   *
   * @return Whether a development card has been played this turn.
   */
  boolean devHasBeenPlayed();

}
