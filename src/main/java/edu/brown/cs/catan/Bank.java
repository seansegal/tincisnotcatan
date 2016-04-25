package edu.brown.cs.catan;

import java.util.Map;

public interface Bank {

  void getResource(Resource resource);
  void getResource(Resource resource, int count);
  void discardResource(Resource resource);
  double getBankRate();
  Map<Resource, Double> getPortRates();

}
