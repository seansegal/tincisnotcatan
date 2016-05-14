package edu.brown.cs.networking;

import java.util.Map;

import com.google.gson.JsonObject;

/**
 * A representation of an API for a single game.
 *
 * @author ndemarco
 */
public interface API {

  /**
   * Get the current game state, with specific or private fields for a player
   * indicated by {@code forPlayer}
   *
   * @param forPlayer
   *          the user identifier integer for this player
   * @return a JsonObject representing the state of the game for this player.
   */
  JsonObject getGameState(int forPlayer);


  /**
   * Perform an action, and receive a mapping of user identifier to JsonObject.
   * The Map returned represents the responses, for each player, given by the
   * action in question.
   *
   * @param action
   *          the String-representation of the desired action, sent by the front
   *          end user.
   * @return a map of id's to response Json for each user.
   */
  Map<Integer, JsonObject> performAction(String action);


  /**
   * Add a player to this game, providing attributes in the form of JSON.
   *
   * @param playerAttributes
   *          fields like "username, color" etc.
   * @return an integer, representing the user's internal identifier for the
   *         rest of this game.
   */
  int addPlayer(JsonObject playerAttributes);


  /**
   * Set any settings for this specific game, like unique rules or limits.
   *
   * @param settings
   *          JsonOBject representing the settings to add.
   */
  void setSettings(JsonObject settings);

}
