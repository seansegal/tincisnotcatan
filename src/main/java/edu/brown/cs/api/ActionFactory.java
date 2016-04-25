package edu.brown.cs.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import edu.brown.cs.actions.Action;
import edu.brown.cs.actions.BuildCity;
import edu.brown.cs.actions.BuildSettlement;
import edu.brown.cs.actions.BuyDevelopmentCard;
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

  private JsonObject convertFromStringToJson(String string) {
    return new Gson().fromJson(string, JsonObject.class);
  }

  public Action createAction(String json) {
    try {
      JsonObject actionJSON = convertFromStringToJson(json);
      return createAction(actionJSON);
    } catch (JsonSyntaxException e) {
      throw new IllegalArgumentException("The JSON contains an error: "
          + e.getLocalizedMessage());
    }
  }

  public Action createAction(JsonObject actionJSON) {
    try {
      String action = actionJSON.get("action").getAsString();
      int playerID = actionJSON.get("player").getAsInt();
      switch (action) {
      case "getInitialState":
        return new EmptyAction();
      case "buildCity":
        return new BuildCity(_referee, playerID,
            toIntersectionCoordinate(actionJSON.get("coordinate")
                .getAsJsonObject()));
      case "buildSettlement":
        return new BuildSettlement(_referee, playerID,
            toIntersectionCoordinate(actionJSON.get("coordinate")
                .getAsJsonObject()), false); // TODO: Referee
      case "buyDevCard":
        return new BuyDevelopmentCard(_referee, playerID);
      case "rollDice":
        return new RollDice(_referee, playerID);
      default:
        String err = String.format("The action %s does not exist.", action);
        throw new IllegalArgumentException(err);
      }

    } catch (NullPointerException e) {
      throw new IllegalArgumentException(
          "The JSON is missing a required parameter. Check documentation for more information.");
    }

  }

  private IntersectionCoordinate toIntersectionCoordinate(JsonObject object) {
    JsonObject coord1 = object.get("coord1").getAsJsonObject();
    JsonObject coord2 = object.get("coord2").getAsJsonObject();
    JsonObject coord3 = object.get("coord3").getAsJsonObject();
    HexCoordinate h1 = new HexCoordinate(coord1.get("x").getAsInt(), coord1
        .get("y").getAsInt(), coord1.get("z").getAsInt());
    HexCoordinate h2 = new HexCoordinate(coord2.get("x").getAsInt(), coord2
        .get("y").getAsInt(), coord2.get("z").getAsInt());
    HexCoordinate h3 = new HexCoordinate(coord3.get("x").getAsInt(), coord3
        .get("y").getAsInt(), coord3.get("z").getAsInt());
    return new IntersectionCoordinate(h1, h2, h3);
  }

}
