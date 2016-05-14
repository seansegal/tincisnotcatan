package edu.brown.cs.networking;

import com.google.gson.JsonObject;

public final class Message {


  private String     sender;

  private int        userId;

  private String     content;

  private long       timeStamp;

  private JsonObject jsonRepresentation;


  public Message(User sender, String content, long timeStamp) {
    this.sender = sender.getField("userName");
    this.userId = sender.userID();
    this.content = content;
    this.timeStamp = timeStamp;
    this.jsonRepresentation = null;
  }


  public JsonObject asJson() {
    if (jsonRepresentation == null) {
      jsonRepresentation = new JsonObject();
      jsonRepresentation.addProperty("requestType", "chat");
      jsonRepresentation.addProperty("userId", userId);
      jsonRepresentation.addProperty("sender", sender);
      jsonRepresentation.addProperty("content", content);
      jsonRepresentation.addProperty("timeStamp", timeStamp);
    }
    return jsonRepresentation;

  }

}
