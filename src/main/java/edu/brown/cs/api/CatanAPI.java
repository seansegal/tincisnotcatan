package edu.brown.cs.api;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import edu.brown.cs.actions.ActionResponse;
import edu.brown.cs.api.CatanConverter.CatanSettings;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.networking.API;

public class CatanAPI implements API {

  public Referee _referee;
  private CatanConverter _converter;
  private ActionFactory _actionFactory;

  // don't add constructor variables to the API without talking to Nick! I use
  // CatanAPI.class.newInstance() which breaks with constructor params.
  public CatanAPI() {
    _referee = new MasterReferee();
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
    synchronized (this) {
      return _converter.getGameState(_referee, playerID);
    }
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
  public int addPlayer(JsonObject playerAttributes) {
    synchronized(this){
      try {
        return _referee.addPlayer(playerAttributes.get("userName").getAsString());
      } catch (JsonSyntaxException | NullPointerException e) {
        throw new IllegalArgumentException(
            "To add a player, you must have userName as a field.");
      }
    }
  }

  /**
   * Should be called after adding a Player. Indicates whether the game has
   * reached it's player limit. If this method returns true, the Start Game
   * action should be called to start a game.
   *
   * @return A boolean indicating that the game is full.
   */
  public boolean gameIsFull() {
    synchronized(this){
      return _referee.gameIsFull();
    }
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
    synchronized (this) {
      if (action == null) {
        throw new IllegalArgumentException("Input cannot be null.");
      }
      try {
        Map<Integer, ActionResponse> responses = _actionFactory.createAction(
            action).execute();
        for (Map.Entry<Integer, ActionResponse> response : responses.entrySet()) {
          response.getValue().addFollowUp(
              _referee.getNextFollowUp(response.getKey()));
        }
        return _converter.responseToJSON(responses);
      }
      catch (IllegalArgumentException e) {
        System.out
            .println("ERROR: Perform Action - " + e.getLocalizedMessage());
        JsonObject json = new JsonObject();
        json.add("requestError",
            new JsonPrimitive("REQUEST ERROR: " + e.getLocalizedMessage()));
        return ImmutableMap.of(-1, json);
      }
      catch(WaitingOnActionException e){
        System.out.println("REACHED");
        return _converter.responseToJSON(e.getResponses());
      }
    }
  }

  public Map<Integer, JsonObject> performAction(JsonObject action) {
    if (action == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    synchronized (this) {
      try {
        Map<Integer, ActionResponse> responses = _actionFactory.createAction(
            action).execute();
        for (Map.Entry<Integer, ActionResponse> response : responses.entrySet()) {
          response.getValue().addFollowUp(
              _referee.getNextFollowUp(response.getKey()));
        }
        return _converter.responseToJSON(responses);
      } catch (IllegalArgumentException e) {
        System.out
            .println("ERROR: Perform Action - " + e.getLocalizedMessage());
        JsonObject json = new JsonObject();
        json.add("requestError",
            new JsonPrimitive("REQUEST ERROR: " + e.getLocalizedMessage()));
        return ImmutableMap.of(-1, json);
      }
      catch(WaitingOnActionException e){
        return _converter.responseToJSON(e.getResponses());
      }
    }
  }

}
