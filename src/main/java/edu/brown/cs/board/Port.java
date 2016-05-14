package edu.brown.cs.board;

import edu.brown.cs.catan.Resource;

/**
 * Class that is for the representation of ports.
 *
 * @author anselvahle
 *
 */
public class Port {
  private Resource _resource;

  /**
   * Constructor for the class.
   *
   * @param resource
   *          Resource that is the type of the port.
   */
  public Port(Resource resource) {
    _resource = resource;
  }

  /**
   * Gets the type of the port.
   * 
   * @return Resource associated with the port.
   */
  public Resource getResource() {
    return _resource;
  }

}
