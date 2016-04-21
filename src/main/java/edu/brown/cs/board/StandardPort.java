package edu.brown.cs.board;

import edu.brown.cs.catan.Resource;

public class StandardPort implements Port {
  private double _exchangeRate;
  private Resource _resource;

  public StandardPort(double exchangeRate, Resource resource) {
    _exchangeRate = exchangeRate;
    _resource = resource;
  }

  @Override
  public Resource getResource() {
    return _resource;
  }
}
