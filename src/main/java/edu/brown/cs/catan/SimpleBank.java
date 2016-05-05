package edu.brown.cs.catan;

import java.util.HashMap;
import java.util.Map;

public class SimpleBank implements Bank {

  Map<Resource, Double> _supply;

  public SimpleBank() {
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
  public double getBankRate() {
    return Settings.BANK_RATE;
  }

  @Override
  public Map<Resource, Double> getPortRates() {
    return Settings.PORT_RATES;
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
}
