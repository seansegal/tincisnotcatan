package edu.brown.cs.hhalvers.catan;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.networking.CatanServer;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

  private static void runSparkServer() throws IOException {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    config.setDirectoryForTemplateLoading(templates);

    FreeMarkerEngine freeMarker = new FreeMarkerEngine(config);

    // Setup spark routes
    Spark.get("/board", new BoardHandler(), freeMarker);
  }

  /**
   * A class which controls the initial page for maps.
   */
  private static class BoardHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title", "Play Catan");
      return new ModelAndView(variables, "board.ftl");
    }
  }

  /**
   * A class which handles exceptions thrown on the server and relays
   * information to the front end.
   */
  private static class ExceptionPrinter implements ExceptionHandler {

    /**
     * The status code for an HTTP internal server error.
     */
    private static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(HTTP_INTERNAL_SERVER_ERROR);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      e.printStackTrace();
      res.body(stacktrace.toString());
    }
  }

  public static void main(String[] args) {
    new CatanServer();
    try {
      runSparkServer();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
