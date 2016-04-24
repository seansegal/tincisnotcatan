package edu.brown.cs.actions;

import java.util.Collections;
import java.util.Map;

public class EmptyAction implements Action {

  @Override
  public Map<Integer, ActionResponse> execute() {
    return Collections.emptyMap();
  }

}
