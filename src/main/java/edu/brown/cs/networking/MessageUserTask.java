package edu.brown.cs.networking;

import static edu.brown.cs.networking.Util.print;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

class MessageUserTask implements Runnable {

  private User   u;
  private String message;
  private GCT    gct;


  public MessageUserTask(User u, String message, GCT gct) {
    this.u = u;
    this.message = message;
    this.gct = gct;
  }


  @Override
  public void run() {
    if (message.equals(Networking.HEARTBEAT)) {
      u.message(Networking.HEARTBEAT_REPLY);
      return; // do nothing else.
    }
    Group g = gct.groupForUser(u);
    JsonObject j = null;
    try {
      j = Networking.GSON.fromJson(message, JsonObject.class);
    } catch (JsonSyntaxException e) {
      print("ERROR parsing json - send error message to client");
      return;
    }
    g.handleMessage(u, j);
  }

}
