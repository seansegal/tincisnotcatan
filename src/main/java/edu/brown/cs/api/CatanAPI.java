package edu.brown.cs.api;

import java.util.Map;

import com.google.gson.JsonObject;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.api.CatanConverter.CatanSettings;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.TestReferee;
import edu.brown.cs.networking.API;

public class CatanAPI implements API {

  public static void main(String[] args) {
    CatanAPI api = new CatanAPI();
    System.out.println(api.getGameState(0));
    System.out.println(api.addPlayer(null));
    System.out.println(api.getGameState(4));
    String json = "{action: rollDice, player: 4}";
    System.out.println(api.performAction(json));
  }

  private Referee _referee;
  private CatanConverter _converter;
  private ActionFactory _actionFactory;

  // don't add constructor variables to the API without talking to Nick! I use
  // CatanAPI.class.newInstance() which breaks with constructor params.
  public CatanAPI() {
//     _referee = new MasterReferee();
    _referee = new TestReferee();
    _converter = new CatanConverter();
    _actionFactory = new ActionFactory(_referee);
  }

  public CatanAPI(String settings) {
    _converter = new CatanConverter();
    CatanSettings catanSettings = _converter.getSettings(settings);
    // TODO: use settings for number of players etc.
    _referee = new MasterReferee();
    _actionFactory = new ActionFactory(_referee);
  }

  @Override
  public JsonObject getGameState(int playerID) {
    return _converter.getGameState(_referee, playerID);
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
  @Override
  public int addPlayer(String playerAttributes) {
    // TODO: decide on playerAttributes, who is choosing colors?
    return _referee.addPlayer("TestName", "#000000");
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
   * Performs Catan Actions. See the README for specific Action JSON
   * documentation.
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
  @Override
  public Map<Integer, JsonObject> performAction(String action) {
    if (action == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    Map<Integer, ActionResponse> response = _actionFactory.createAction(action)
        .execute();

    return _converter.responseToJSON(response);
  }

}
