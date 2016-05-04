package edu.brown.cs.catan;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trade {
  private final int _trader;
  private final Set<Integer> _acceptedTrade;
  private final Set<Integer> _declinedTrade;
  private final Map<Resource, Double> _resources;
  
  public Trade(int trader, Map<Resource, Double> resources) {
    _trader = trader;
    _resources = resources;
    _acceptedTrade = new HashSet<>();
    _declinedTrade = new HashSet<>();
  }

  public void acceptedTrade(int playerID) {
    _acceptedTrade.add(playerID);
  }

  public void declinedTrade(int playerID) {
    _declinedTrade.add(playerID);
  }

  public int getTrader() {
    return _trader;
  }

  public Set<Integer> getAcceptedTrade() {
    return Collections.unmodifiableSet(_acceptedTrade);
  }

  public Map<Resource, Double> getResources() {
    return Collections.unmodifiableMap(_resources);
  }

}
