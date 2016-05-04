package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.catan.Bank;
import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlayYearOfPlenty implements Action {
  private Referee _ref;
  private Player _player;
  private Resource _firstRes;
  private Resource _secondRes;
  private Bank _bank;

  public PlayYearOfPlenty(Referee ref, int playerID, String firstRes,
      String secondRes) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    _bank = _ref.getBank();
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _firstRes = Resource.stringToResource(firstRes);
    _secondRes = Resource.stringToResource(secondRes);
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
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
    try {
      _player.playDevelopmentCard(DevelopmentCard.YEAR_OF_PLENTY);
    } catch (IllegalArgumentException e) {
      ActionResponse toAdd = new ActionResponse(false,
          "You don't have a Year of Plenty card", null);
      Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
      toRet.put(_player.getID(), toAdd);
      return toRet;
    }

    _player.addResource(_firstRes);
    _player.addResource(_secondRes);
    // Update Bank stats
    _bank.getResource(_firstRes);
    _bank.getResource(_secondRes);
    String message = "";
    if (_firstRes == _secondRes) {
      message = String.format("You gained 2 %s", _firstRes);
    } else {
      message = String.format("You gained %s and %s.", _firstRes.stringWithArticle(),
          _secondRes.stringWithArticle());
    }
    ActionResponse toAdd = new ActionResponse(true, message,
        new YearOfPlentyResponse(_firstRes, _secondRes));
    Map<Integer, ActionResponse> toRet = new HashMap<Integer, ActionResponse>();
    toRet.put(_player.getID(), toAdd);
    return toRet;
  }

  private static class YearOfPlentyResponse {
    private Resource firstRes;
    private Resource secondRes;

    public YearOfPlentyResponse(Resource first, Resource second) {
      firstRes = first;
      secondRes = second;
    }

  }

}
