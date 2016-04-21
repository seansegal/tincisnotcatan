package edu.brown.cs.actions;

import java.util.Map;

import edu.brown.cs.board.Intersection;
import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Settings;

public class BuildSettlement implements Action {

  private Player _player;
  private Intersection _intersection;
  private boolean _mustPay;
  private Referee _ref;

  public BuildSettlement(Referee ref, int playerID, IntersectionCoordinate i, boolean mustPay) {
    _ref = ref;
    _player = ref.getPlayerByID(playerID);
    _intersection = ref.getBoard().getIntersections().get(i);
    _mustPay = mustPay;
  }

  @Override
  public ActionResponse execute() {
    if(_mustPay) {
      Map<Resource, Double> resources = _player.getResources();
     Settings.SETTLEMENT_COST.forEach((res, amount) -> {
       if(resources.get(res) < amount){
         //TODO: throw error
       }
     });
     _player.buildSettlement();
    }
    //TODO: add turn validation, add graph validation
    _player.useSettlement();
    _intersection.placeSettlement(_player);

    return null;
  }



}
