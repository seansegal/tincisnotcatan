package edu.brown.cs.networking;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Networking {

  public static final String     USER_IDENTIFIER             = "USER_ID";
  public static final String     REQUEST_IDENTIFIER          = "requestType";
  public static final String     HEARTBEAT                   = "\"HEARTBEAT\"";

  public static final long       ONE_SECOND                  = 1000;
  public static final long       DISCONNECT_TIMEOUT          = ONE_SECOND * 60;

  // to be used throughout the Networking package unless conflicts
  // exist with registerTypeAdapter
  public static final Gson       GSON                        = new GsonBuilder()
      .registerTypeAdapter(Group.class, new GroupSerializer())
      .registerTypeAdapter(User.class, new UserSerializer())
      .create();

  // to send when previously disconnected users have reconnected.
  public static final JsonObject GAME_READY_MESSAGE          =
      new JsonObjectBuilder()
          .addProperty(REQUEST_IDENTIFIER, "disconnectedUsers")
          .add("users", GSON.toJsonTree(Collections.emptyList()))
          .addProperty("expiresAt", -1)
          .build();

  // to be sent when the game is full and about to start.
  public static final JsonObject START_GAME_MESSAGE          =
      new JsonObjectBuilder()
          .addProperty(REQUEST_IDENTIFIER, "action")
          .addProperty("action", "startGame")
          .build();

  public static final JsonObject GAME_OVER_DISCONNECTED_USER =
      new JsonObjectBuilder()
          .addProperty(REQUEST_IDENTIFIER, "gameOver")
          .addProperty("reason", "disconnectedUser")
          .build();

  public static final JsonObject HEARTBEAT_REPLY             =
      new JsonObjectBuilder()
          .addProperty(REQUEST_IDENTIFIER, "heartbeat")
          .build();


  public static JsonObject userDisconnectedMessage(final Map<User, Long> disc) {
    JsonObject message = new JsonObject();
    Set<User> disconUsers = new HashSet<>();
    long smallestExpire = Long.MAX_VALUE;
    for (User u : disc.keySet()) {
      if (disc.get(u) < smallestExpire) {
        smallestExpire = disc.get(u);
      }
      disconUsers.add(u);
    }
    message.addProperty(REQUEST_IDENTIFIER, "disconnectedUsers");
    message.add("users", GSON.toJsonTree(disconUsers));
    message.addProperty("expiresAt", smallestExpire);
    return message;
  }

  public static JsonObject errorMessage(String reason) {
    JsonObject j = new JsonObject();
    j.addProperty(REQUEST_IDENTIFIER, "ERROR");
    j.addProperty("description", reason);
    return j;

  }

}
