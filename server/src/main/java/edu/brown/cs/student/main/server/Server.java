package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.handlers.AddPinsHandler;
import edu.brown.cs.student.main.server.handlers.ClearPins;
import edu.brown.cs.student.main.server.handlers.GeoDataHandler;
import edu.brown.cs.student.main.server.handlers.KeyWordSearchHandler;
import edu.brown.cs.student.main.server.handlers.getPinsHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import spark.Filter;
import spark.Spark;

/** Top Level class for our project, utilizes spark to create and maintain our server. */
public class Server {

  public static void setUpServer() {
    int port = 3232;
    Spark.port(port);

    after(
        (Filter)
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    StorageInterface firebaseUtils;
    try {
      firebaseUtils = new FirebaseUtilities();

      Spark.get("add-pins", new AddPinsHandler(firebaseUtils));
      Spark.get("list-pins", new getPinsHandler(firebaseUtils));
      Spark.get("clear-pins", new ClearPins(firebaseUtils));
      Spark.get("geo-data", new GeoDataHandler());
      Spark.get("keyword", new KeyWordSearchHandler());

      Spark.notFound(
          (request, response) -> {
            response.status(404); // Not Found
            System.out.println("ERROR");
            return "404 Not Found - The requested endpoint does not exist.";
          });
      Spark.init();
      Spark.awaitInitialization();

      System.out.println("Server started at http://localhost:" + port);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(
          "Error: Could not initialize Firebase. Likely due to firebase_config.json not being found. Exiting.");
      System.exit(1);
    }
  }

  /**
   * Runs Server.
   *
   * @param args none
   */
  public static void main(String[] args) {
    setUpServer();
  }
}
