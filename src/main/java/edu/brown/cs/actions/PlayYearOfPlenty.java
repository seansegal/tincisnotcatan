package edu.brown.cs.actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import edu.brown.cs.catan.Bank;
import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlayYearOfPlenty implements Action {
  private final Referee _ref;
  private final Player _player;
  private final Bank _bank;
  private final Map<Resource, Double> _resources;
  private final static double TOLERANCE = .01;
  public static final String ID = "playYearOfPlenty";

  public PlayYearOfPlenty(Referee ref, int playerID, JsonObject params) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _bank = _ref.getBank();
    JsonObject trade = params.get("resources").getAsJsonObject();
    Map<Resource, Double> resources = new HashMap<>();
    for (Resource res : Resource.values()) {
      if (trade.has(res.toString())) {
        resources.put(res, CatanFormats.round(trade.get(res.toString()).getAsDouble()));
      }
    }
    _resources = Collections.unmodifiableMap(resources);
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (!(_player.getDevCards().get(DevelopmentCard.YEAR_OF_PLENTY) > 0)) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You don't have a Year of Plenty card", null));
    }
    if (_ref.getTurn().devHasBeenPlayed()) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You already played a development card this turn", null));
    }
    if (!_ref.getTurn().hadInitialDevCard(DevelopmentCard.YEAR_OF_PLENTY)) {
      return ImmutableMap
          .of(_player.getID(), new ActionResponse(false,
              "You cannot play a development card on the turn you bought it",
              null));
    }
    double resCount = 0.0;
    for (Double count : _resources.values()) {
      resCount += count;
    }
    if(_resources != null) {
      if (2 - resCount > TOLERANCE || 2 - resCount < 0.0) {
        return ImmutableMap.of(_player.getID(), new ActionResponse(false,
            "You did not select the proper amount of resources", null));
      }
    }

    try {
      _player.playDevelopmentCard(DevelopmentCard.YEAR_OF_PLENTY);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "You don't have a Year of Plenty card", null);
      Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }

    StringBuilder message = new StringBuilder("You gained:");

    if (_ref.getGameSettings().isDecimal) {
      NumberFormat nf = new DecimalFormat("##.##");
      for (Resource res : _resources.keySet()) {
        if (_resources.get(res) > 0.0) {
          _player.addResource(res, _resources.get(res), _bank);
          message.append(String.format(" %s %s,",
              nf.format(_resources.get(res)), res));
        }
      }
    } else {
      for (Resource res : _resources.keySet()) {
        if (_resources.get(res) > 0) {
          _player.addResource(res, _resources.get(res), _bank);
          message.append(String.format(" %d %s,", _resources.get(res), res));
        }
      }
    }

    message.replace(message.toString().length() - 1, message.toString()
        .length(), "");

    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    toRet.put(_player.getID(), new ActionResponse(true, message.toString(),
        _resources));
    return toRet;
  }

}
