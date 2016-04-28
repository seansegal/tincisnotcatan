package edu.brown.cs.actions;

public class ActionResponse {

  private boolean success;
  private String message;
  private FollowUpActionRaw followUpAction;
  private Object data;

  public ActionResponse(boolean success, String message, Object data) {
    super();
    this.success = success;
    this.message = message;
    this.followUpAction = null;
    this.data = data;
  }

  // public ActionResponse(boolean success, String message, String
  // followUpAction,
  // Object data) {
  // super();
  // this.success = success;
  // this.message = message;
  // this.followUpAction = followUpAction;
  // this.data = data;
  // }

  public void addFollowUp(FollowUpAction followUpAction) {
    if (followUpAction != null) {
      this.followUpAction = new FollowUpActionRaw(followUpAction);
    }
  }

  private static class FollowUpActionRaw {

    private String actionName;
    private Object actionData;

    public FollowUpActionRaw(FollowUpAction followUp) {
      actionName = followUp.getID();
      actionData = followUp.getData();
    }
  }
}
