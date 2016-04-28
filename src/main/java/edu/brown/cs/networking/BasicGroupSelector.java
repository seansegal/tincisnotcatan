package edu.brown.cs.networking;

import java.util.Collection;

import edu.brown.cs.networking.Group;
import edu.brown.cs.networking.UserGroup.UserGroupBuilder;

// a simple sorter that takes no preferences of the end user into account,
// and simply filters groups by whether or not they're full.
class BasicGroupSelector implements GroupSelector {

  @Override
  public Group selectFor(User u, Collection<Group> coll) {
    for(Group ug : coll) {
      if (!ug.isFull()) {
        return ug;
      }
    }
    UserGroupBuilder b = new UserGroupBuilder(BasicAPI.class);
    return b.build();
  }

}
