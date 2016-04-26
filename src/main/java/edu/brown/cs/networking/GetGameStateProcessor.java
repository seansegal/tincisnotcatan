package edu.brown.cs.networking;

import java.util.Collection;

import com.google.gson.JsonObject;


public class GetGameStateProcessor implements RequestProcessor {

  private static final String IDENTIFIER = "getGameState";

  private final API api;

  public GetGameStateProcessor(API api) {
    this.api = api;
  }

  @Override
  public boolean run(User<?> user, Collection<User<?>> group, JsonObject json) {
    JsonObject resp = api.getGameState(user.userID());
    resp.addProperty("requestType", "getGameState");
    return user.message(resp);
  }


  @Override
  public String identifier() {
    return IDENTIFIER;
  }

}
