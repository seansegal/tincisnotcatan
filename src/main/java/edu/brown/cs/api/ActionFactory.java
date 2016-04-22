package edu.brown.cs.api;

import edu.brown.cs.actions.Action;
import edu.brown.cs.catan.Referee;

public class ActionFactory {

  private Referee _referee;

  public ActionFactory(Referee referee){
    _referee = referee;
  }

  public Action createAction(String json){
    //TODO: Create Actions based on JSON
    return null;
  }



}
