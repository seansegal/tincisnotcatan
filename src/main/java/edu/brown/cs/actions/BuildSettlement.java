package edu.brown.cs.actions;

import java.util.Map;

import edu.brown.cs.board.Intersection;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Resource;
import edu.brown.cs.catan.Settings;

public class BuildSettlement implements Action {

  private Player _player;
  private Intersection _intersection;
  private boolean _mustPay;

  public BuildSettlement(Player p, Intersection i, boolean mustPay) {
    _player = p;
    _intersection = i;
    _mustPay = mustPay;
  }

  @Override
  public void execute() {
    if(_mustPay) {
      Map<Resource, Double> resources = _player.getResources();
     Settings.SETTLEMENT_COST.forEach((res, amount) -> {
       if(resources.get(res) < amount){
         //TODO: throw error
       }
     });
     _player.buildSettlement();
    }
    _player.useSettlement();
    //TODO: add settlement to the board

  }

}
