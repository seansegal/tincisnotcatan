package edu.brown.cs.catan;

import java.util.Collections;
import java.util.List;

import edu.brown.cs.board.Intersection;

public class Setup {

  private Intersection _lastBuiltSettlement;
  private int _currentTurn;
  private List<Integer> _setupOrder;

  public Setup(List<Integer> setupOrder){
    _lastBuiltSettlement = null;
    _currentTurn = 0;
    _setupOrder = setupOrder;
  }

  public List<Integer> getSetupOrder() {
    return Collections.unmodifiableList(_setupOrder);
  }

  public void set_setupOrder(List<Integer> _setupOrder) {
    this._setupOrder = _setupOrder;
  }

  public void setLastBuiltSettlement(Intersection i){
    _lastBuiltSettlement = i;
  }

  public void nextTurn(){
    _currentTurn++;
  }

  public Intersection getLastBuiltSettlement(){
    return _lastBuiltSettlement;
  }

  public int getCurrentPlayerID(){
    return _setupOrder.get(_currentTurn);
  }

}
