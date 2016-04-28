package edu.brown.cs.networking;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DistinctRandom {

  private static Set<String> usedAlready = new HashSet<>();

  public static String getString() {
    String newid = UUID.randomUUID().toString();
    while(usedAlready.contains(newid)) {
      newid = UUID.randomUUID().toString();
    }
    usedAlready.add(newid);
    return newid;
  }

}
