package edu.brown.cs.networking;

import java.util.Collection;

/**
 * Used to choose the ideal Group (selected from a list of non-full Groups that
 * have at least one User in them in the GCT). The GroupSelector can access any
 * field of the Groups in making this determination, including the consideration
 * of unique identifiers that may have been requested by the end user. (In the
 * case of joining an existing game). c that u should be placed in.
 *
 * @author ndemarco
 */
public interface GroupSelector {

  /**
   * Find the best Group in {@code coll} for {@code u}, else create a new Group
   * and return it.
   *
   * @param u
   *          the new {@code User} for which to find or make a Group.
   * @param coll
   *          a collection of {@code Group}s that are pending (not full), given
   *          by the GCT.
   * @return the ideal group for {@code u}. Should never be null.
   */
  Group selectFor(User u, Collection<Group> coll);

}
