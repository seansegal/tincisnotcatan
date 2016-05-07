package edu.brown.cs.actions;

public class ActionResponse {

  private boolean success;
  private String message;
  private Object data;

  public ActionResponse(boolean success, String message, Object data) {
    super();
    this.success = success;
    this.message = message;
    this.data = data;
  }

  public boolean getSuccess() {
    return success;
  }

}
