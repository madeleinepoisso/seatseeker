package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class getEventsHandler implements Route {

  public StorageInterface storageHandler;

  public getEventsHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * Invoked when a request is made on this route's corresponding path e.g. '/hello'
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      String uid = request.queryParams("uid");
      if (uid == null || uid.isEmpty()) {
        return Utils.toMoshiJson(
            Map.of("status", "failure", "message", "User ID (uid) is required."));
      }

      List<Map<String, Object>> events = storageHandler.getCollection(uid);
      if (events.isEmpty()) {
        return Utils.toMoshiJson(
            Map.of(
                "status", "success", "message", "No saved events found for user.", "data", events));
      } else {
        return Utils.toMoshiJson(Map.of("status", "success", "data", events));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Utils.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
