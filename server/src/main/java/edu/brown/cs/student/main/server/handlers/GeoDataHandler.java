package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.parserNestedClasses.GeoJsonObject;
import edu.brown.cs.student.main.server.parserNestedClasses.JsonParser2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import spark.Request;
import spark.Response;
import spark.Route;

public class GeoDataHandler implements Route {
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
      // collect parameters from the request
      String maxLat = request.queryParams("maxLat");
      String minLat = request.queryParams("minLat");
      String maxLong = request.queryParams("maxLong");
      String minLong = request.queryParams("minLong");

      JsonParser2 jsonParser = new JsonParser2();
      GeoJsonObject geoJsonObject = jsonParser.createGeoJson();

      List<GeoJsonObject.Feature> filteredFeatures =
          geoJsonObject.features.stream()
              .filter(feature -> isFeatureWithinBounds(feature, minLat, maxLat, minLong, maxLong))
              .collect(Collectors.toList());

      responseMap.put("response_type", "success");
      responseMap.put("features", filteredFeatures);

    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("error", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }

  /**
   * Checks if a given feature is within the specified bounds.
   *
   * @param feature The feature to check.
   * @param minLat The minimum latitude.
   * @param maxLat The maximum latitude.
   * @param minLong The minimum longitude.
   * @param maxLong The maximum longitude.
   * @return True if the feature is within the bounds, false otherwise.
   */
  private boolean isFeatureWithinBounds(
      GeoJsonObject.Feature feature, String minLat, String maxLat, String minLong, String maxLong) {
    if (feature.geometry == null) {
      System.out.println("the geometry is empty");
      return false;
    }
    List<List<List<List<Double>>>> coordinates = feature.geometry.coordinates;

    if (coordinates != null && !coordinates.isEmpty()) {
      List<List<Double>> polygon = coordinates.get(0).get(0);
      if (polygon != null && !polygon.isEmpty()) {
        return polygon.stream()
            .allMatch(
                point -> {
                  double lat = point.get(1);
                  double lon = point.get(0);
                  return lat >= Double.parseDouble(minLat)
                      && lat <= Double.parseDouble(maxLat)
                      && lon >= Double.parseDouble(minLong)
                      && lon <= Double.parseDouble(maxLong);
                });
      }
    }
    return false;
  }
}
