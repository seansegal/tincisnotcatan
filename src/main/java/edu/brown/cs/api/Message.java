package edu.brown.cs.api;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

import edu.brown.cs.networking.User;

public class Message {

  @Expose
  private String     sender;
  @Expose
  private String     content;
  @Expose
  private long       timeStamp;

  private JsonObject jsonRepresentation;


  public Message(User sender, String content, long timeStamp) {
    this.sender = sender.getField("userName");
    this.content = content;
    this.timeStamp = timeStamp;
    this.jsonRepresentation = null;
  }


  public JsonObject asJson() {
    if(jsonRepresentation == null) {
      jsonRepresentation = new JsonObject();
      jsonRepresentation.addProperty("sender", sender);
      jsonRepresentation.addProperty("content", content);
      jsonRepresentation.addProperty("timeStamp", timeStamp);
    }
    return jsonRepresentation;

  }

}
