package edu.brown.cs.catan;

import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of Bank that offers Dyanmic rates based on Resource supply.
 * Uses a logit function as to represent the possible distribution of rates.
 * Rates vary from MIN_RATE to MAX_RATE which are constants that could be
 * changed. Uses both the expectation of a given Resource in the game and the
 * relative amount of a given Resource to determine the rate.
 *
 *
 */
public class DynamicBank implements Bank {

  private Map<Resource, Double> _supply;
  private static final double MIN_RATE = 2.0;
  private static final double MAX_RATE = 6.0;
  private static final double EXPECTATION = 3.0; // The expected number of a
                                                 // given resource.

  public static void main(String[] args) {
    // System.out.println(new DynamicBank().getRateFromProbit(10));
  }

  /**
   * Creates a Dynamic Bank.
   */
  public DynamicBank() {
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
    // assert newVal >= 0;
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
    // assert newVal >= 0;
    _supply.put(resource, newVal);
  }

  @Override
  public double getBankRate(Resource res) {
    double max = 0.0;
    double min = Double.MAX_VALUE;
    for (Resource resource : Resource.values()) {
      if (getScoreForResource(resource) > max) {
        max = getScoreForResource(resource);
      }
      if (getScoreForResource(resource) < min) {
        min = getScoreForResource(resource);
      }
    }
    return Math
        .round(getRateFromProbit((getScoreForResource(res) - (max / 2.0))) * 10.0) / 10.0;
  }

  @Override
  public double getPortRate(Resource res) {
    return Math.round((0.5) * getBankRate(res) * 10.0) / 10.0;
  }

  @Override
  public double getWildCardRate(Resource res) {
    return Math.round((0.75) * getBankRate(res) * 10.0) / 10.0;
  }

  private double getRateFromProbit(double x) {
    return ((MAX_RATE - MIN_RATE) / (1 + Math.exp(-x))) + MIN_RATE;
  }

  private double getScoreForResource(Resource res) {
    return getResourceRatio(res) * getExpectationRatio(res);
  }

  private double getResourceRatio(Resource res) {
    double count = 0.0;
    for (double supply : _supply.values()) {
      count += supply;
    }
    if (count != 0.0) {
      return _supply.get(res) / count;
    }
    return 0.0;
  }

  private double getExpectationRatio(Resource res) {
    return _supply.get(res) / getExpectation(res);
  }

  private double getExpectation(Resource res) {
    return EXPECTATION;
  }

}
