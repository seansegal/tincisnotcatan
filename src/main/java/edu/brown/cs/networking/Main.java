package edu.brown.cs.networking;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

  public static void main(String[] args) {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.port(getHerokuAssignedPort());
    // secure("", "", "", ""); // use this for https!
    GCT.getInstance();

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

    // secure("", "", "", ""); // use this for https!
    Spark.init();

  }

  // used for heroku hosting - environment variables are set by heroku.
  private static int getHerokuAssignedPort() {
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
