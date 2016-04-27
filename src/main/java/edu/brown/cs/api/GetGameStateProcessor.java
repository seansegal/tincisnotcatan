package edu.brown.cs.api;

import java.util.Collection;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.API;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;


public class GetGameStateProcessor implements RequestProcessor {

  private static final String IDENTIFIER = "getGameState";
  private static final String REQUEST_KEY = "requestType";


  @Override
  public boolean run(User user, Collection<User> group, JsonObject json,
      API api) {
    JsonObject resp = api.getGameState(user.userID());
    resp.addProperty("requestType", "getGameState");
    return user.message(resp);
  }


  @Override
  public boolean match(JsonObject j) {
    if(j.has(REQUEST_KEY) && !j.get(REQUEST_KEY).isJsonNull()){
      return j.get(REQUEST_KEY).getAsString().equals(IDENTIFIER);
    }
    return false;
  }

}
