package edu.brown.cs.catan;

import java.util.Map;

public interface Bank {

  void getResource(Resource resource);
  void getResource(Resource resource, double count);
  void discardResource(Resource resource);
  void discardResource(Resource resource, double count);
  double getBankRate();
  Map<Resource, Double> getPortRates();

}
