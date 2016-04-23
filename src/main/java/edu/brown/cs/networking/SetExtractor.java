package edu.brown.cs.networking;

import java.util.Iterator;
import java.util.Set;

public class SetExtractor {

  public static <T> T getSingleElement(Set<T> set) {
    Iterator<T> iter = set.iterator();
    if(!iter.hasNext()){
      return null;
    }
    T element = iter.next();
    if(iter.hasNext()) {
      throw new AssertionError("Tried to getSingleElement from multi-element set");
    }
    return element;
  }

}
