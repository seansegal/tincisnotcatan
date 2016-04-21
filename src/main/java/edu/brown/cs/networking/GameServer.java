package edu.brown.cs.networking;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

// arbitrary server class for N users that have to register, etc.
final class GameServer {

  // use the singleton pattern! This server should only ever have
  // one connection to the web / queries, etc.
  private static GameServer    sharedInstance = new GameServer();

  // all of the Sessions that are connected to this server.
  private Map<Session, String> userIds        = new HashMap<>();

  // a simple counter for userIDs - to remove?
  private int                  nextUserID     = 1;


  // initialize the game server
  public static GameServer getInstance() {
    return sharedInstance;
  }


  private GameServer() {}


  public void launch() {
    try {
      Spark.externalStaticFileLocation("src/main/resources/static");
      Spark.port(getHerokuAssignedPort());

      Configuration config = new Configuration();
      File templates = new File(
          "src/main/resources/spark/template/freemarker");
      config.setDirectoryForTemplateLoading(templates);
      FreeMarkerEngine freeMarker = new FreeMarkerEngine(config);

      // set up chat
      Spark.webSocket("/chat", ChatWebSocketHandler.class);
      Spark.webSocket("/action", ActionWebSocketHandler.class);

      // Set up board
      Spark.get("/board", new BoardHandler(), freeMarker);

      // secure("", "", "", ""); // use this for https!
      Spark.init();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  // Sends a message from one user to all users, along with a list of current
  // usernames
  public void broadcastMessage(String sender, String message) {

    // a session is a single user interacting with the server.
    for (Session session : userIds.keySet()) {
      if (session.isOpen()) { // if the session is still active.

        // Return a reference to the RemoteEndpoint object representing
        // the
        // other end of this conversation.
        RemoteEndpoint remEnd = session.getRemote();

        // create the JSONObject we want to send.
        // this could be anything!
        JSONObject toSend = Chat.createMessage(sender, message,
            userIds.values());

        // send the string to the session
        try {
          remEnd.sendString(String.valueOf(toSend));
        } catch (IOException e) {
          System.out
              .println("IOException while sending JSON to session.");
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * Indicate if a given session is in our chat.
   *
   * @param user
   *          the Session object
   * @return true if this {@code user} is in the chat.
   */
  public boolean contains(Session user) {
    return userIds.containsKey(user);
  }


  public int activeUsers() {
    return userIds.keySet().size();
  }


  public int getAndIncrementId() {
    return nextUserID++;
  }


  public boolean remove(Session toRemove) {
    return userIds.remove(toRemove) != null;
  }


  public boolean add(Session toAdd, String id) {
    return userIds.put(toAdd, id) != id;
  }


  public String get(Session toGet) {
    return userIds.get(toGet);
  }


  // used for heroku hosting - environment variables are set by heroku.
  private int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on
    // localhost)
  }


  /**
   * A class which controls the initial page for maps.
   */
  private static class BoardHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Play Catan");
      return new ModelAndView(variables, "board.ftl");
    }
  }

}
