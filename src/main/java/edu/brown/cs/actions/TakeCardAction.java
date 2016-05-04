package edu.brown.cs.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class TakeCardAction implements Action, FollowUpAction {

  public final static String ID = "takeCard";
  private final static String VERB = "take a card";
  private final int _playerID;
  private boolean _isSetup;
  private Collection<Integer> _toTake;
  private int _playerToTake;
  private Referee _ref;

  public TakeCardAction(int playerID, Collection<Integer> toTake) {
    assert toTake.size() > 0;
    _isSetup = false;
    _playerID = playerID;
    _toTake = toTake;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    Player player = _ref.getPlayerByID(_playerID);
    Player playerToTakeFrom = _ref.getPlayerByID(_playerToTake);
    // Validation:
    if (!_isSetup) {
      throw new UnsupportedOperationException(
          "A FollowUpAction must be setup before it is executed.");
    }
    if (!_toTake.contains(_playerToTake)) {
      System.out.println("HERE with toTake: " + _toTake + " and playerToTake:" + _playerToTake);
      return ImmutableMap
          .of(_playerID,
              new ActionResponse(
                  false,
                  "You must take from a player adjacent to the Tile where you placed the Robber.",
                  null));
    }
    List<Resource> takeableCards = new ArrayList<>();
    double count;
    for (Map.Entry<Resource, Double> res : playerToTakeFrom.getResources()
        .entrySet()) {
      count = res.getValue();
      while(count >= 1.0){
        count -= 1.0;
        takeableCards.add(res.getKey());
      }
    }
    if (takeableCards.isEmpty()) {
      return ImmutableMap
          .of(_playerID,
              new ActionResponse(
                  false,
                  "The player you are trying to take from doesn't have enough resources.",
                  null));
    }
    // Action:
    Collections.shuffle(takeableCards);
    Resource resToTake = takeableCards.get(0);
    player.addResource(resToTake);
    playerToTakeFrom.removeResource(resToTake);
    _ref.removeFollowUp(this);

    // Format responses:
    String messageToPlayer = String.format("You stole %s from %s!",
        resToTake.stringWithArticle(), playerToTakeFrom.getName());
    ActionResponse respToPlayer = new ActionResponse(true, messageToPlayer,
        resToTake);
    String messageToStolen = String.format("You lost %s",
        resToTake.stringWithArticle());
    ActionResponse respToStolen = new ActionResponse(true, messageToStolen,
        resToTake);
    String messageToAll = String.format("%s stole a card from %s",
        player.getName(), playerToTakeFrom.getName());
    ActionResponse respToAll = new ActionResponse(true, messageToAll, null);
    Map<Integer, ActionResponse> toReturn = new HashMap<>();
    for (Player p : _ref.getPlayers()) {
      if (p.equals(player)) {
        toReturn.put(p.getID(), respToPlayer);
      } else if (p.equals(playerToTakeFrom)) {
        toReturn.put(p.getID(), respToStolen);
      } else {
        toReturn.put(p.getID(), respToAll);
      }
    }
    return toReturn;
  }

  @Override
  public JsonObject getData() {
    JsonObject json = new JsonObject();
    JsonArray playerIDs = new JsonArray();
    for(int id : _toTake){
      playerIDs.add(new JsonPrimitive(id));
    }
    json.add("toTake", playerIDs);
    json.addProperty("message", "Please pick an opponent to steal a card from.");
    return json;
  }

  @Override
  public String getID() {
    return TakeCardAction.ID;
  }

  @Override
  public int getPlayerID() {
    return _playerID;
  }

  @Override
  public void setupAction(Referee ref, int playerID, JsonObject json) {
    try{
      _playerToTake = json.get("takeFrom").getAsInt();
      _isSetup = true;
      _ref = ref;
    }
    catch(NullPointerException e){
      throw new IllegalArgumentException("expecting takeFrom, must missing in JSON");
    }

  }

  @Override
  public String getVerb() {
    return VERB;
  }

}
