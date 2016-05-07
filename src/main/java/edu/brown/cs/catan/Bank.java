package edu.brown.cs.catan;



public interface Bank {

  void getResource(Resource resource);
  void getResource(Resource resource, double count);
  void discardResource(Resource resource);
  void discardResource(Resource resource, double count);
  double getBankRate(Resource res);
  double getPortRate(Resource res);
//  Map<Resource, Double> getPortRates();

}
