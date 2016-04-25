package edu.brown.cs.networking;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

class Chat {

  private static final Gson GSON = new Gson();


  private Chat() {}


  public static JsonObject createMessage(String sender, String message,
      Collection<String> userIds) {
    JsonObject toSend = new JsonObject();

    // build the values
    toSend.add("requestType", GSON.toJsonTree("chat"));
    toSend.add("userMessage", createHtmlMessageFromSender(sender, message));
    toSend.add("userList", GSON.toJsonTree(userIds));

    return toSend;
  }


  // Builds a HTML element with a sender-name, a message, and a timestamp,
  private static JsonElement createHtmlMessageFromSender(String sender,
      String message) {
    return GSON.toJsonTree(
        article()
            .with(b(sender + ":"), p(message),
                span().withClass("timestamp")
                    .withText(
                        new SimpleDateFormat("HH:mm:ss").format(new Date())))
            .render());
  }

}
