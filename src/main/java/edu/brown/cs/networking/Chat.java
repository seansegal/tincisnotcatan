package edu.brown.cs.networking;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Chat {

  private Chat() {}

  public static JSONObject createMessage(String sender, String message, Collection<String> userIds) {
    JSONObject toSend = new JSONObject();
    try {
      // build the values
      toSend
          .put("userMessage", createHtmlMessageFromSender(sender, message))
          .put("userList", userIds);
    } catch (JSONException e) {
      System.out.println("JSON exception in broadcastMessage");
      e.printStackTrace();
    }
    return toSend;
  }


  // Builds a HTML element with a sender-name, a message, and a timestamp,
  private static String createHtmlMessageFromSender(String sender,
      String message) {
    return article().with(
        b(sender + " says:"),
        p(message),
        span().withClass("timestamp")
            .withText(new SimpleDateFormat("HH:mm:ss").format(new Date())))
        .render();
  }

}
