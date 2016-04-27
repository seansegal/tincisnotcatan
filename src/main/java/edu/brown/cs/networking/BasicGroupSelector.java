package edu.brown.cs.networking;

import java.util.Collection;

import edu.brown.cs.networking.UserGroup.UserGroupBuilder;

// a simple sorter that takes no preferences of the end user into account,
// and simply filters groups by whether or not they're full.
public class BasicGroupSelector implements GroupSelector {

  @Override
  public UserGroup selectFor(User u, Collection<UserGroup> coll) {
    for(UserGroup ug : coll) {
      if (!ug.isFull()) {
        return ug;
      }
    }
    UserGroupBuilder b = new UserGroupBuilder(null);
    return b.build();
  }

}
