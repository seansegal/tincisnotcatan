package edu.brown.cs.api;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.actions.ActionResponse;

public class WaitingOnActionException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private int _playerID = -1;
  private final String _message;


  public WaitingOnActionException(String message, int playerID) {
    super(message);
    _message = message;
    _playerID = playerID;
  }

  public Map<Integer, ActionResponse> getResponses(){
    return ImmutableMap.of(_playerID, new ActionResponse(false, _message, null));
  }

}
