package edu.brown.cs.networking;

class ConnectUserTask implements Runnable {

  private User u;
  private GCT  gct;


  public ConnectUserTask(User u, GCT gct) {
    this.u = u;
    this.gct = gct;
  }


  @Override
  public void run() {
    Group g = gct.groupForUser(u);
    if (g == null) {
      gct.add(u);
    } else {
      g.add(u);
    }

  }

}
