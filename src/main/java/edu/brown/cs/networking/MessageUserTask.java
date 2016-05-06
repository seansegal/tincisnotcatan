package edu.brown.cs.networking;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class MessageUserTask implements Runnable {

  private User   u;
  private String message;
  private GCT    gct;
  private static final Gson GSON = new Gson();


  public MessageUserTask(User u, String message, GCT gct) {
    this.u = u;
    this.message = message;
    this.gct = gct;
  }


  @Override
  public void run() {
    Group g = gct.groupForUser(u);
    JsonObject j = null;
    try {
      j = GSON.fromJson(message, JsonObject.class);
    } catch (JsonSyntaxException e) {
      System.out.println("ERROR parsing json - send error message to client");
      return;
    }
    g.handleMessage(u, j);
  }

}
