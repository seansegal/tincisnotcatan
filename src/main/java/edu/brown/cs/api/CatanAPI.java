package edu.brown.cs.api;

import java.util.Map;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.TestReferee;

public class CatanAPI {

  public static void main(String[] args) {
    System.out.println(new CatanAPI().getGameState(0));
  }

  private Referee _referee;
  private CatanConverter _converter;
  private ActionFactory _actionFactory;

  public CatanAPI() {
    _referee = new TestReferee();
    _converter = new CatanConverter();
  }

  public CatanAPI(String settings) {
    // TODO: parse and set settings
    _referee = new TestReferee();
    _converter = new CatanConverter();
  }

  public String getGameState(int playerID) {
    return _converter.getGameState(_referee, playerID);
  }

  @Deprecated
  public String getBoard() {
    return _converter.getBoard(_referee.getBoard());
  }

  @Deprecated
  public String getHand(int playerID) {
    return _converter.getHand(_referee.getPlayerByID(playerID));
  }

  @Deprecated
  public String getPlayers() {
    return _converter.getPlayers(_referee.getReadOnlyReferee());
  }

  /**
   * Adds a player to a game of Catan. This should, and can, only be called
   * before a game has started. It is important that gameIsFull() is called
   * directly after this method is called.
   *
   * @param playerAttributes
   *          A JSON String representing the player's attributes. Should contain
   *          a "name" and "color" field.
   * @return The new player's unique player ID.
   * @throws IllegalArgumentException
   *           When JSON string cannot be parsed or is missing a player
   *           attribute.
   * @throws UnsupportedOperationException
   *           When called in the middle of a game.
   */
  public int addPlayer(String playerAttributes) {

    // if (name == null || color == null) {
    // throw new IllegalArgumentException("Inputs cannot be null.");
    // }
    // return _referee.addPlayer(name, color);
    return -1;
  }

  /**
   * Should be called after adding a Player. Indicates whether the game has
   * reached it's player limit. If this method returns true, the Start Game
   * action should be called to start a game.
   *
   * @return A boolean indicating that the game is full.
   */
  public boolean gameIsFull() {
    return _referee.gameIsFull();
  }

  /**
   * Performs Catan Actions.
   *
   * @param action
   *          A JSON String with parameters for a given Catan action.
   * @return A Map from Player IDs to JSON Strings that should be sent as a
   *         response. If the Map contains -1 as a key, this indicates that an
   *         error occurred with the request. This could be caused by a bad or
   *         poorly formed request. For example, if the JSON is missing a given
   *         attribute for an action -1 will be returned and it will map to a
   *         more specific message as to why the Action failed.
   */
  public Map<Integer, String> performAction(String action) {
    if (action == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    Map<Integer, ActionResponse> response = _actionFactory.createAction(action)
        .execute();
    return _converter.responseToJSON(response);
  }

}
