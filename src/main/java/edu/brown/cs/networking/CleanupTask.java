package edu.brown.cs.networking;


public class CleanupTask implements Runnable {


  private User      u;
  private UserTable table;


  public CleanupTask(User u, UserTable t) {
    this.u = u;
    this.table = t;
  }


  @Override
  public void run() {
    try {
      if (table.expired(u)) {
        // game ends, remove user, etc?
      } else {
        // sleep
      }
    } catch (ExpiredUserException e) {
      // thread consistency error, can just kill the thread.
    }

  }


}
