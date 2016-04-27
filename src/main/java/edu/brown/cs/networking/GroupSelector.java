package edu.brown.cs.networking;

import java.util.Collection;

// Using parameters of User that the end-developer can predict,
// we're able to select a user group from a collection that fits
// the preferences of the game or problem specific to the end-developer.
// returns null if no such group fits the parameters specified.
public interface GroupSelector {

  UserGroup selectFor(User u, Collection<UserGroup> coll);

}
