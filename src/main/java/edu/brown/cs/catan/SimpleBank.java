package edu.brown.cs.catan;

import java.util.Map;

public class SimpleBank implements Bank {

  Map<Resource, Double> _supply;

  public SimpleBank() {}

  @Override
  public void getResource(Resource resource) {}

  @Override
  public void discardResource(Resource resource) {}

  @Override
  public void getResource(Resource resource, double count) {}

  @Override
  public void discardResource(Resource resource, double count) {}

  @Override
  public double getBankRate(Resource res) {
    return Settings.BANK_RATE;
  }

  @Override
  public double getPortRate(Resource res) {
    return Settings.PORT_RATES.get(res);
  }

  @Override
  public double getWildCardRate(Resource res) {
    return Settings.WILDCARD_RATE;
  }
}
