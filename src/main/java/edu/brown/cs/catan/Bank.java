package edu.brown.cs.catan;

public interface Bank {

  void getResource(Resource resource);
  void discardResource(Resource resource);
  double getBankRate();

}
