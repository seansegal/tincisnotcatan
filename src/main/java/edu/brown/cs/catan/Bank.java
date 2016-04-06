package edu.brown.cs.catan;

public interface Bank {

  // Map<Resource,Integer> supply;

  void getResource(Resource resource);
  void discardResource(Resource resource);

}
