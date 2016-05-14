package edu.brown.cs.catan;

/**
 * Represents a Catan Bank. A Bank must give bank trade and port trade rates. A
 * Bank is given access to the resource supply.
 *
 */
public interface Bank {

  /**
   * Called when a new resource is taken from the Resource deck.
   *
   * @param resource
   *          The Resource type.
   */
  void getResource(Resource resource);

  /**
   * Called when multiple of one resource is taken from the Resource deck.
   *
   * @param resource
   *          The Resource type
   * @param count
   *          The number of Resources
   */
  void getResource(Resource resource, double count);

  /**
   * Called when a resource is discarded or used by a Player and put back into
   * the Resource deck.
   *
   * @param resource
   *          The Resource type
   */
  void discardResource(Resource resource);

  /**
   * Called when many of one resource is discared or used by a Player and put
   * back into the Resource deck.
   *
   * @param resource
   *          The Resource type
   * @param count
   *          The number of resources
   */
  void discardResource(Resource resource, double count);

  /**
   * Returns the general bank rate for a given resource. This is the rate
   * Players receive when they do not have a Port.
   *
   * @param res
   *          The Resource to trade in
   * @return The number of the input resource required to get one of any card.
   */
  double getBankRate(Resource res);

  /**
   * Returns the port rate for a given resource. This is the rate a Player who
   * is on a specific resource Port would receive.
   *
   * @param res
   *          The Resource to trade in
   * @return The number of the input resource required to get one full resource
   *         of any type.
   */
  double getPortRate(Resource res);

  /**
   * Returns the port rate given to Players on a WildCard port (The ?).
   *
   * @param res
   *          The Resource to trade in
   * @return The number of the input resource required to get one full resource
   *         of any type.
   */
  double getWildCardRate(Resource res);

}
