package edu.brown.cs.networking;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Provides a static method getString(), which provides a guaranteed-unique
 * alphanumeric string for user or group identifiers.
 *
 * @author ndemarco
 */
public class DistinctRandom {

  private static Set<String> usedAlready = new HashSet<>();


  /**
   * @return a random string, distinct from any other string so far returned by
   *         this method.
   */
  public static String getString() {
    String newid = UUID.randomUUID().toString();
    while (usedAlready.contains(newid)) {
      newid = UUID.randomUUID().toString();
    }
    usedAlready.add(newid);
    return newid;
  }

}
