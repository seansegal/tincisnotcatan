package edu.brown.cs.actions;

public class ActionResponse {

  private boolean success;
  private String message;
  private String followUpAction;
  private Object data;

  public ActionResponse(boolean success, String message, Object data) {
    super();
    this.success = success;
    this.message = message;
    this.followUpAction = null;
    this.data = data;
  }

//  public ActionResponse(boolean success, String message, String followUpAction,
//      Object data) {
//    super();
//    this.success = success;
//    this.message = message;
//    this.followUpAction = followUpAction;
//    this.data = data;
//  }

  public void addFollowUp(String followUpAction) {
    this.followUpAction = followUpAction;
  }

}
