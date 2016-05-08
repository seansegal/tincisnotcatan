package edu.brown.cs.networking;

public class DisconnectUserTask implements Runnable {

  private User   u;
  @SuppressWarnings("unused")
  private int    statusCode;
  @SuppressWarnings("unused")
  private String reason;
  private GCT    gct;


  public DisconnectUserTask(User u, int statusCode, String reason, GCT gct) {
    this.u = u;
    this.statusCode = statusCode;
    this.reason = reason;
    this.gct = gct;
  }


  @Override
  public void run() {
    Group g = gct.groupForUser(u);
    g.remove(u);
  }
}
