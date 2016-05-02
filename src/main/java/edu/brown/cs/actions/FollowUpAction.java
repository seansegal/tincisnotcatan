package edu.brown.cs.actions;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.Referee;

public interface FollowUpAction extends Action {

  JsonObject getData();

  String getID();

  int getPlayerID();

  void setupAction(Referee ref, int playerID, JsonObject params);

  String getVerb();

}
