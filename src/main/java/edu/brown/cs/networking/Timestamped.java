package edu.brown.cs.networking;


public interface Timestamped extends Comparable<Timestamped> {

  long initTime();

  void stampNow();

  @Override
  default int compareTo(Timestamped t) {
    long myInit = initTime();
    long theirInit = t.initTime();
    if(myInit < theirInit) {
      return -1;
    }
    if(myInit > theirInit) {
      return 1;
    }
    return 0;
  }

}
