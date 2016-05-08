package edu.brown.cs.catan;

import java.util.HashMap;
import java.util.Map;


public class DynamicBank implements Bank {


  Map<Resource, Double> _supply;

  public DynamicBank() {
    System.out.println("DYNAMIC BANK CREATED");
    _supply = new HashMap<>();
    for (Resource resource : Resource.values()) {
      _supply.put(resource, 0.0);
    }
  }

  @Override
  public void getResource(Resource resource) {
    double newVal = _supply.get(resource) + 1.0;
    _supply.put(resource, newVal);
  }

  @Override
  public void discardResource(Resource resource) {
    double newVal = _supply.get(resource) - 1.0;
//    assert newVal >= 0;
    _supply.put(resource, newVal);
  }

  @Override
  public void getResource(Resource resource, double count) {
    double newVal = _supply.get(resource) + count;
    _supply.put(resource, newVal);
  }

  @Override
  public void discardResource(Resource resource, double count) {
    double newVal = _supply.get(resource) - count;
//    assert newVal >= 0;
    _supply.put(resource, newVal);
  }

  @Override
  public double getBankRate(Resource res) {
    //TODO
    return 5.8;
  }

  @Override
  public double getPortRate(Resource res) {
    return Math.round((0.5)*getBankRate(res)*10.0)/10.0;
  }

  @Override
  public double getWildCardRate(Resource res) {
    return Math.round((0.75)*getBankRate(res)*10.0)/10.0;
  }

  private double getRateFromProbit(double x){
    return (4/(1 + Math.exp(x))) + 2.0;
  }





}
