package edu.brown.cs.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.brown.cs.networking.DistinctRandom;
import edu.brown.cs.networking.Group;
import edu.brown.cs.networking.GroupSelector;
import edu.brown.cs.networking.RequestProcessor;
import edu.brown.cs.networking.User;
import edu.brown.cs.networking.UserGroup.UserGroupBuilder;


public class CatanGroupSelector implements GroupSelector {

  private static final String                NUM_PLAYERS = "numPlayersDesired";
  private final Collection<RequestProcessor> catanProcessors;


  public CatanGroupSelector() {
    catanProcessors = new ArrayList<>();
    catanProcessors.add(new GetGameStateProcessor());
    catanProcessors.add(new ActionProcessor());
    catanProcessors.add(new ChatProcessor());
  }


  @Override
  public Group selectFor(User u, Collection<Group> coll) {
    int desiredSize = Integer.parseInt(u.getField(NUM_PLAYERS));
    for (Group ug : coll) {
      if (!ug.isFull() && ug.maxSize() == desiredSize) {
        return ug;
      }
    }
    return new UserGroupBuilder(CatanAPI.class)
        .withSize(desiredSize)
        .withRequestProcessors(
            Collections.unmodifiableCollection(catanProcessors))
        .withUniqueIdentifier(DistinctRandom.getString())
        .build();
  }

}
