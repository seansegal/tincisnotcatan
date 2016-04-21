package edu.brown.cs.board;

import edu.brown.cs.catan.Resource;

public class Port {
  private Resource _resource;

  public Port(Resource resource) {
    _resource = resource;
  }

  public Resource getResource() {
    return _resource;
  }

}
