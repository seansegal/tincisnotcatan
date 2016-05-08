package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.google.gson.JsonObject;

import edu.brown.cs.networking.DistinctRandom;
import edu.brown.cs.networking.Group;
import edu.brown.cs.networking.GroupSelector;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;
import edu.brown.cs.networking.UserGroup.UserGroupBuilder;

public class CatanGroupSelector implements GroupSelector {

  private static final String                NUM_PLAYERS          =
      "numPlayersDesired";
  private static final String                VICTORY_POINTS       =
      "victoryPoints";
  private static final String                IS_DECIMAL           =
      "isDecimal";
  private static final String                GAME_REQUEST_ID      =
      "desiredGroupId";
  private static final String                GAME_NAME_IDENTIFIER = "groupName";
  private final Collection<RequestProcessor> catanProcessors;


  public CatanGroupSelector() {
    catanProcessors = new ArrayList<>();
    catanProcessors.add(new GetGameStateProcessor());
    catanProcessors.add(new ActionProcessor());
    catanProcessors.add(new ChatProcessor());
    catanProcessors.add(new GameOverProcessor());
  }


  @Override
  public Group selectFor(User u, Collection<Group> coll) {
    Optional<Group> usersExistingGroup =
        coll.stream().filter(g -> g.hasUser(u)).findFirst();
    if (usersExistingGroup.isPresent()) {
      return usersExistingGroup.get();
    }

    if (u.getFieldsAsJson().has(GAME_REQUEST_ID)) {
      Optional<Group> requested = coll.stream()
          .filter(ug -> ug.isFull()
              && ug.identifier().equals(u.getField(GAME_REQUEST_ID)))
          .findFirst();
      if (requested.isPresent()) {
        return requested.get();
      }
      return null;
    }

    int desiredSize = Integer.parseInt(u.getField(NUM_PLAYERS));
    if (desiredSize < 2 || desiredSize > 4) {
      System.out
          .println("ERROR: Size requested out of bounds : " + desiredSize);
      return null;
    }
    int victoryPoints = Integer.parseInt(u.getField(VICTORY_POINTS));
    boolean isDecimal = Boolean.parseBoolean(u.getField(IS_DECIMAL));

    // name the game
    String name = u.hasField(GAME_NAME_IDENTIFIER)
        ? u.getField(GAME_NAME_IDENTIFIER) : "Unnamed game";

    // MAKE SETTINGS :
    JsonObject settings = new JsonObject();
    settings.addProperty("numPlayers", desiredSize);
    settings.addProperty("victoryPoints", victoryPoints);
    settings.addProperty("isDecimal", isDecimal);

    return new UserGroupBuilder(CatanAPI.class)
        .withSize(desiredSize)
        .withRequestProcessors(
            Collections.unmodifiableCollection(catanProcessors))
        .withName(name)
        .withApiSettings(settings)
        .withUniqueIdentifier(DistinctRandom.getString()).build();
  }

}
