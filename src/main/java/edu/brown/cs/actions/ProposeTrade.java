package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Trade;

public class ProposeTrade implements Action{
  private Player _player;
  private Referee _ref;
  private Map<Resource, Double> _resources;

  public ProposeTrade(Referee ref, int playerID, JsonObject params) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    if (!ref.currentPlayer().equals(_player)) {
      throw new IllegalArgumentException();
    }
    JsonObject trade = params.get("trade").getAsJsonObject();
    Map<Resource, Double> resources = new HashMap<Resource, Double>();
    for (Resource res : Resource.values()) {
      if (trade.has(res.toString())) {
        resources.put(res, trade.get(res.toString()).getAsDouble());
      }
    }
    _resources = Collections.unmodifiableMap(resources);
  }


  @Override
  public Map<Integer, ActionResponse> execute() {
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    Collection<FollowUpAction> toDoNext = new ArrayList<FollowUpAction>();
    boolean containsNeg = false;
    boolean containsPos = false;
    for(double d : _resources.values()) {
      if(d > 0) {
        containsPos = true;
      } else if(d < 0) {
        containsNeg = true;
      }
    }
    if (_resources.values().size() < 2 || !containsNeg || !containsPos) {
      ActionResponse toAdd = new ActionResponse(false,
          "You must exchange at least one resource for at least one other resource.",
          _resources);
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }
    Trade trade = new Trade(_player.getID(), _resources);

    for (Player p : _ref.getPlayers()) {
      if (!p.equals(_player)) {
        FollowUpAction toDo = new ReviewTrade(p.getID(), _resources, trade);
        toDoNext.add(toDo);
        String message = String.format("%s wants to trade", _player.getName());
        ActionResponse toAdd = new ActionResponse(true, message, _resources);
        toRet.put(p.getID(), toAdd);
      } else if (p.equals(_player)) {
        FollowUpAction toDo = new TradeResponse(_player.getID(), _resources,
            trade);
        toDoNext.add(toDo);
        String message = "See who wants to trade with you.";
        ActionResponse toAdd = new ActionResponse(true, message, _resources);
        toRet.put(p.getID(), toAdd);
      }
    }
    _ref.addFollowUp(toDoNext);
    return toRet;
  }

}
