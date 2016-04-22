package edu.brown.cs.actions;

import java.util.Map;

import edu.brown.cs.board.IntersectionCoordinate;
import edu.brown.cs.catan.Player;
import edu.brown.cs.catan.Referee;

public class BuildRoad implements Action {

  // Where will validation occur?
  private Referee _ref;
  private Player _player;

  public BuildRoad(Referee ref, int playerID, IntersectionCoordinate start, IntersectionCoordinate end){
    //TODO;
  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    // TODO Auto-generated method stub
    return null;

  }

}
