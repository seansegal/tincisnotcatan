package edu.brown.cs.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edu.brown.cs.actions.Action;
import edu.brown.cs.actions.BuildSettlement;
import edu.brown.cs.actions.EmptyAction;
import edu.brown.cs.actions.RollDice;
import edu.brown.cs.board.HexCoordinate;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.MasterReferee;
import edu.brown.cs.catan.Referee;

public class ActionFactory {

  private Referee _referee;

  public ActionFactory(Referee referee) {
    assert referee != null;
    assert referee instanceof MasterReferee;
    _referee = referee;
  }

  public Action createAction(String json) {
    try {
      JsonObject actionJSON = new Gson().fromJson(json, JsonObject.class);
      String action = actionJSON.get("action").getAsString();
      int playerID = actionJSON.get("player").getAsInt();
      switch (action) {
      case "getInitialState":
        return new EmptyAction();
      case "buildSettlement":
        JsonObject coord1 = actionJSON.get("coordinate").getAsJsonObject()
            .get("coord1").getAsJsonObject();
        JsonObject coord2 = actionJSON.get("coordinate").getAsJsonObject()
            .get("coord2").getAsJsonObject();
        JsonObject coord3 = actionJSON.get("coordinate").getAsJsonObject()
            .get("coord3").getAsJsonObject();
        HexCoordinate h1 = new HexCoordinate(coord1.get("x").getAsInt(), coord1
            .get("y").getAsInt(), coord1.get("z").getAsInt());
        HexCoordinate h2 = new HexCoordinate(coord2.get("x").getAsInt(), coord2
            .get("y").getAsInt(), coord2.get("z").getAsInt());
        HexCoordinate h3 = new HexCoordinate(coord3.get("x").getAsInt(), coord3
            .get("y").getAsInt(), coord3.get("z").getAsInt());
        return new BuildSettlement(_referee, playerID,
            new IntersectionCoordinate(h1, h2, h3), false); // TODO: change so
                                                            // that referee says
                                                            // if they pay.
      case "rollDice":
        return new RollDice(_referee, playerID);
      default:
        String err = String.format("The action %s does not exist.", action);
        throw new IllegalArgumentException(err);
      }

    } catch (Exception e) {
      throw new IllegalArgumentException("Could not parse the JSON.");
    }

  }
}
