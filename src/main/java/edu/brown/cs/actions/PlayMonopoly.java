package edu.brown.cs.actions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlayMonopoly implements Action {
  private Referee _ref;
  private Player _player;
  private Resource _res;

  public PlayMonopoly(Referee ref, int playerID, String res) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _res = Resource.stringToResource(res);
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    if (_ref.getTurn().devHasBeenPlayed()) {
      return ImmutableMap.of(_player.getID(), new ActionResponse(false,
          "You already played a development card this turn", null));
    }
    if (!_ref.getTurn().hadInitialDevCard(DevelopmentCard.MONOPOLY)) {
      return ImmutableMap
          .of(_player.getID(), new ActionResponse(false,
              "You cannot play a development card on the turn you bought it",
              null));
    }
    try {
      _player.playDevelopmentCard(DevelopmentCard.MONOPOLY);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "You don't have a Monopoly card", null);
      Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    double totalResCount = 0;
    for (Player otherPlayer : _ref.getPlayers()) {
      if (!otherPlayer.equals(_player)) {
        double numResource = otherPlayer.getResources().get(_res);
        totalResCount += numResource;
        otherPlayer.removeResource(_res, numResource, _ref.getBank());
        NumberFormat nf = new DecimalFormat("##.##");
        String message = String.format(
            "%s played a Monopoly card. You lost %s %s.", _player.getName(),
            nf.format(numResource), _res.toString());
        Map<Resource, Double> resourceMap = new HashMap<Resource, Double>();
        resourceMap.put(_res, numResource);
        ActionResponse toAdd = new ActionResponse(true, message, resourceMap);
        toRet.put(otherPlayer.getID(), toAdd);
      }
    }
    _player.addResource(_res, totalResCount, _ref.getBank());
    Map<Resource, Double> resourceMap = new HashMap<Resource, Double>();
    resourceMap.put(_res, totalResCount);
    NumberFormat nf = new DecimalFormat("##.##");
    String message = String.format(
        "You played a Monopoly card and gained %s %s", nf.format(totalResCount),
        _res.toString());
    ActionResponse toAdd = new ActionResponse(true, message, resourceMap);
    toRet.put(_player.getID(), toAdd);
    return toRet;
  }

}
