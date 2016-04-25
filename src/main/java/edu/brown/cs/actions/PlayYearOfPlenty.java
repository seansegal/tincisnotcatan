package edu.brown.cs.actions;

import java.util.HashMap;
import java.util.Map;

import edu.brown.cs.catan.DevelopmentCard;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;

public class PlayYearOfPlenty implements Action {
  private Referee _ref;
  private Player _player;
  private Resource _firstRes;
  private Resource _secondRes;
  
  public PlayYearOfPlenty(Referee ref, int playerID, Resource firstRes,
      Resource secondRes) {
    assert ref != null;
    _ref = ref;
    _player = _ref.getPlayerByID(playerID);
    if (_player == null) {
      String err = String.format("No player exists with the id: %d", playerID);
      throw new IllegalArgumentException(err);
    }
    _firstRes = firstRes;
    _secondRes = secondRes;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
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
    String message = String.format("You gained a %1 and a %2.", _firstRes,
        _secondRes);
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
