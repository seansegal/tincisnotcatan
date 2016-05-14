package edu.brown.cs.networking;

import com.google.gson.JsonObject;

/**
 * The GCT makes no assumptions about the format of messages that the developer
 * intends to receive from the front end. A RequestProcessor allows the
 * end-developer to programmatically define what messages to accept and how to
 * handle said messages.
 *
 * @author ndemarco
 */
public interface RequestProcessor {

  /**
   * Handle the message {@code json}. It is assumed that {@code match()}
   * returned true before this method is called.
   *
   * @param user
   *          the user that sent the message
   * @param g
   *          the group that contains {@code user}
   * @param json
   *          the message
   * @param api
   *          the API / game manager for this group.
   * @return true indicating if the message processing was successful.
   */
  boolean run(User user, Group g, JsonObject json, API api);


  /**
   * Indicate if {@code json} is in a format such that this RequestProcessor
   * should handle the message.
   *
   * @param json
   *          the message
   * @return true if this RequestProcessor should handle this message.
   */
  boolean match(JsonObject json);


}
