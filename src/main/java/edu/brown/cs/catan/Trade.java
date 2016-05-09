package edu.brown.cs.catan;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.util.ConcurrentHashSet;

public class Trade {
  private final int _trader;
  private final ConcurrentHashSet<Integer> _acceptedTrade;
  private final ConcurrentHashSet<Integer> _declinedTrade;
  private final Map<Resource, Double> _resources;
  
  public Trade(int trader, Map<Resource, Double> resources) {
    _trader = trader;
    _resources = resources;
    _acceptedTrade = new ConcurrentHashSet<>();
    _declinedTrade = new ConcurrentHashSet<>();
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

  public Set<Integer> getDeclinedTrade() {
    return Collections.unmodifiableSet(_declinedTrade);
  }

  public Map<Resource, Double> getResources() {
    return Collections.unmodifiableMap(_resources);
  }

}
