package edu.brown.cs.actions;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.Referee;

/**
 * Interface for how FollowUp actions should act.
 *
 * @author anselvahle
 *
 */
public interface FollowUpAction extends Action {

  JsonObject getData();

  String getID();

  int getPlayerID();

  void setupAction(Referee ref, int playerID, JsonObject params);

  String getVerb();

}
