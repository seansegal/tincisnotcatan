package edu.brown.cs.networking;

import java.util.Map;

import com.google.gson.JsonObject;

/**
 * A representation of an API for a single game.
 * @author ndemarco
 *
 */
public interface API {

  JsonObject getGameState(int forPlayer);

  Map<Integer, JsonObject> performAction(String action);

  int addPlayer(JsonObject playerAttributes);

  void setSettings(JsonObject settings);

}
