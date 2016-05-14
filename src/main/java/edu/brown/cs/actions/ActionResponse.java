package edu.brown.cs.actions;

/**
 * General form for reponse from Actions.
 *
 * @author anselvahle
 *
 */
public class ActionResponse {

  private boolean success;
  private String message;
  private Object data;

  /**
   * Constructor for the class.
   * 
   * @param success
   *          Boolean stating whether or not the action succeeded.
   * @param message
   *          Message for the player about the action.
   * @param data
   *          Information specific to the action.
   */
  public ActionResponse(boolean success, String message, Object data) {
    super();
    this.success = success;
    this.message = message;
    this.data = data;
  }

  /**
   * Getter for success.
   * 
   * @return success.
   */
  public boolean getSuccess() {
    return success;
  }

}
