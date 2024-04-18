package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.parserNestedClasses.GeoJsonObject;
import edu.brown.cs.student.main.server.parserNestedClasses.JsonParser2;
import edu.brown.cs.student.main.server.parserNestedClasses.MockGeoJsonObject;
import edu.brown.cs.student.main.server.parserNestedClasses.MockJsonParser2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class KeyWordSearchHandler implements Route {
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

      String keyword = request.queryParams("toSearch");

      if ("coke_mock".equals(keyword)) {
        System.out.println("entering");
        MockJsonParser2 mockJsonParser = new MockJsonParser2();
        MockGeoJsonObject mockGeoJsonObject = mockJsonParser.createGeoJson();
        System.out.println("coke");
        System.out.println(mockGeoJsonObject.features);
        responseMap.put("response_type", "success");
        responseMap.put("features", mockGeoJsonObject.features);
      }

      JsonParser2 jsonParser = new JsonParser2();
      GeoJsonObject geoJsonObject = jsonParser.createGeoJson();

      List<GeoJsonObject.Feature> filteredDescriptions =
          geoJsonObject.features.stream()
              .filter(feature -> doesContainWord(feature.properties, keyword))
              .collect(Collectors.toList());

      responseMap.put("response_type", "success");
      responseMap.put("features", filteredDescriptions);

    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("error", e.getMessage());
    }

    // return Utils.toMoshiJson(responseMap);
    String jsonResponse = Utils.toMoshiJson(responseMap);
    System.out.println(jsonResponse);
    return jsonResponse;
  }

  /**
   * Checks if the provided properties contain the specified keyword.
   *
   * @param properties The properties to search
   * @param keyword The keyword to search for
   * @return True if the keyword is found in the properties, false otherwise.
   */
  private boolean doesContainWord(GeoJsonObject.Properties properties, String keyword) {
    for (String key : properties.area_description_data.keySet()) {
      String description = properties.area_description_data.get(key);
      if (description != null && description.toLowerCase().contains(keyword.toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
