package edu.brown.cs.actions;

import java.util.Map;

/**
 * Interface for Actions
 * 
 * @author anselvahle
 *
 */
public interface Action {

  /**
   * Executes the action
   * 
   * @return A Map of Player IDs to some specific action response.
   */
  Map<Integer, ActionResponse> execute();

}
