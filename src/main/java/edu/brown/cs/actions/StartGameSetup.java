package edu.brown.cs.actions;

import java.util.List;
import java.util.Map;

import edu.brown.cs.catan.Referee;

public class StartGameSetup implements Action {

  private Referee _ref;

  public StartGameSetup(Referee ref) {

  }

  @Override
  public Map<Integer, ActionResponse> execute() {
    List<Integer> turnOrder = _ref.getTurnOrder();
//    for()
//    return null;
  }

}
