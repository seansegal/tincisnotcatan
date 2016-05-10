package edu.brown.cs.catan;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.api.CatanGroupSelector;
import edu.brown.cs.networking.GCT;
import edu.brown.cs.networking.GCT.GCTBuilder;
import edu.brown.cs.networking.Networking;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

  private static final int    NUM_THREADS      = 8;
  private static final int    MIN_THREADS      = 2;
  private static final int    TIMEOUT          = 3600000;

  private static final String STATIC_FILE_PATH =
      System.getenv("HEROKU") != null ? "target/classes/static"
          : "src/main/resources/static";

  private GCT                 gct;


  public static void main(String[] args) {
    new Main().run();
  }


  private Main() {
    Spark.externalStaticFileLocation(STATIC_FILE_PATH);
    Spark.port(getHerokuAssignedPort());
    Spark.threadPool(NUM_THREADS, MIN_THREADS, TIMEOUT);
    // secure("", "", "", ""); // use this for https!
    gct = new GCTBuilder("/action")
        .withGroupSelector(new CatanGroupSelector())
        .withGroupViewRoute("/groups")
        .build();

    Configuration config = new Configuration();
    File templates = new File(
        "src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    FreeMarkerEngine freeMarker = new FreeMarkerEngine(config);

    // Set up board
    Spark.get("/board", new BoardHandler(), freeMarker);
    Spark.get("/home", new HomeHandler(), freeMarker);
    Spark.get("stats", new StatsHandler(), freeMarker);
    Spark.before("/", (request, response) -> {
      System.out.println(
          "Redirect causes an extra open/close on GroupView. Disregard.");
      response.redirect("/home");
    });

    // secure("", "", "", ""); // use this for https!

    Spark.init();
  }


  private void run() {}


  // used for heroku hosting - environment variables are set by heroku.
  private static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on
    // localhost)
  }


  private class StatsHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables =
          new ImmutableMap.Builder<String, Object>()
              .put("title", "Catan Stats")
              .put("openGroups", gct.openGroups().toString())
              .put("closedGroups", gct.closedGroups().toString())
              .put("limit", gct.groupLimit())
              .build();
      return new ModelAndView(variables, "stats.ftl");
    }

  }

  private static class BoardHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Play Catan");
      return new ModelAndView(variables, "board.ftl");
    }
  }

  /**
   * A class which controls the initial page for maps.
   */
  private class HomeHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, String> cookies = req.cookies();
      if (cookies.containsKey(Networking.USER_IDENTIFIER)) {
        System.out.println("1");
        if (Main.this.gct
            .userIDIsValid(cookies.get(Networking.USER_IDENTIFIER))) {
          System.out.println("2");
          res.redirect("/board");
          return new BoardHandler().handle(req, res);
        }
      }

      Map<String, Object> variables = ImmutableMap.of("title",
          "Catan : Home");
      return new ModelAndView(variables, "home.ftl");
    }
  }

}
