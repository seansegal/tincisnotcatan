package edu.brown.cs.actions;

import java.util.Map;

public interface Action {

  Map<Integer, ActionResponse> execute(); // what to return/does it throw
                                          // ActionException?

}
