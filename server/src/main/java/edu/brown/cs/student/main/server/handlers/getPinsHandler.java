package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class getPinsHandler implements Route {

  public StorageInterface storageHandler;

  public getPinsHandler(StorageInterface storageHandler) {
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

      List<Map<String, Object>> pins = storageHandler.getCollection(uid);
      if (pins.isEmpty()) {
        return Utils.toMoshiJson(
            Map.of("status", "success", "message", "No pins found for user.", "data", pins));
      } else {
        return Utils.toMoshiJson(Map.of("status", "success", "data", pins));
      }
    } catch (Exception e) {
      e.printStackTrace();
      return Utils.toMoshiJson(Map.of("status", "failure", "message", e.getMessage()));
    }
  }
}
