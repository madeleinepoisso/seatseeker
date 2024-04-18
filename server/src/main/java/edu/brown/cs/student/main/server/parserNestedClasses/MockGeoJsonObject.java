package edu.brown.cs.student.main.server.parserNestedClasses;

import java.util.List;
import java.util.Map;

public class MockGeoJsonObject {
  public String response_type;
  public List<Feature> features;

  public static class Feature {
    public String type;
    public Geometry geometry;
    public Properties properties;
  }

  public static class Geometry {
    public String type;
    public List<List<List<List<Double>>>> coordinates;
  }

  public static class Properties {
    public String city;
    public String holc_grade;
    public Map<String, String> area_description_data;
  }
}
