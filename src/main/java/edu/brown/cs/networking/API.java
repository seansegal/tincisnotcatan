package edu.brown.cs.networking;

import java.util.Map;

import com.google.gson.JsonObject;

public interface API {

  Class<? extends UserData> getUserDataClass();

  JsonObject getGameState(int forPlayer);

  Map<Integer, JsonObject> performAction(String action);

  int addPlayer(String playerAttributes);

}
