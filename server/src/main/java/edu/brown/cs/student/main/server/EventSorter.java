package edu.brown.cs.student.main.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.brown.cs.student.main.server.parserNestedClasses.JsonParser2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventSorter {

  public static void insertionSort(List<Event> bucket) {
    for (int i = 1; i < bucket.size(); ++i) {
      Event key = bucket.get(i);
      float keyPrice = key.price();
      int j = i - 1;
      while (j >= 0 && bucket.get(j).price() > keyPrice) {
        bucket.set(j + 1, bucket.get(j));
        j--;
      }
      bucket.set(j + 1, key);
    }
  }

  public static int withinRange(String city, String searchCity) {
    String api_url1 = "https://api.api-ninjas.com/v1/geocoding?city=" + city;
    String api_url2 = "https://api.api-ninjas.com/v1/geocoding?city=" + searchCity;
    try {

      URL url1 = new URL(api_url1);
      HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
      connection1.setRequestMethod("GET");
      connection1.setRequestProperty("Authorization", "JClbpgBLBGqlkM2vQerDbQ==E64E5q17qvCeNtCU");
      connection1.connect();

      URL url2 = new URL(api_url2);
      HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
      connection2.setRequestMethod("GET");
      connection2.setRequestProperty("Authorization", "JClbpgBLBGqlkM2vQerDbQ==E64E5q17qvCeNtCU");
      connection2.connect();

      int responseCode1 = connection1.getResponseCode();
      int responseCode2 = connection2.getResponseCode();
      if (responseCode1 == HttpURLConnection.HTTP_OK
          && responseCode2 == HttpURLConnection.HTTP_OK) {
        BufferedReader reader1 =
            new BufferedReader(new InputStreamReader(connection1.getInputStream()));
        BufferedReader reader2 =
            new BufferedReader(new InputStreamReader(connection2.getInputStream()));

        StringBuilder response1 = new StringBuilder();
        StringBuilder response2 = new StringBuilder();
        String line;
        while ((line = reader1.readLine()) != null) {
          response1.append(line);
        }
        while ((line = reader2.readLine()) != null) {
          response2.append(line);
        }

        String city_data = response1.toString();
        String search_city_data = response2.toString();

        JsonParser2 parser = new JsonParser2();

        JsonArray cityArray = parser.fromJsonGeneral(city_data, JsonArray.class);
        JsonArray searchCityArray = parser.fromJsonGeneral(search_city_data, JsonArray.class);

        JsonObject cityObject = cityArray.get(0).getAsJsonObject();
        JsonObject searchCityObject = searchCityArray.get(0).getAsJsonObject();

        double city_lat = cityObject.get("latitude").getAsDouble();
        double city_lon = cityObject.get("longitude").getAsDouble();
        double search_city_lat = searchCityObject.get("latitude").getAsDouble();
        double search_city_lon = searchCityObject.get("longitude").getAsDouble();

        reader1.close();
        reader2.close();

        connection1.disconnect();
        connection2.disconnect();

        double R = 6371.0;

        double lat1 = city_lat * Math.PI / 180;
        double lon1 = city_lon * Math.PI / 180;
        double lat2 = search_city_lat * Math.PI / 180;
        double lon2 = search_city_lon * Math.PI / 180;

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a =
            Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance_km = R * c;
        double distance_miles = distance_km * 0.621371;

        if (distance_miles <= 50) {
          return 1;
        } else {
          return 0;
        }
      } else {
        return 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  public static ArrayList<Event> bucketSort(
      List<Event> events, String date, String time, String city) {
    Event[] eventArray = events.toArray(new Event[0]);
    int n = events.size();

    ArrayList<Event>[] buckets = new ArrayList[4];
    for (int i = 0; i < buckets.length; i++) {
      buckets[i] = new ArrayList<>();
    }

    for (int i = 0; i < n; i++) {
      int val = 0;
      if (date != null) {
        if (eventArray[i].date.equals(date)) {
          val++;
        }
      }
      if (time != null) {
        if (eventArray[i].time.equals(time)) {
          val++;
        }
      }
      if (city != null) {
        if (withinRange(city, eventArray[i].city) == 1) {
          val++;
        }
      }
      buckets[val].add(eventArray[i]);
    }

    for (int i = 0; i < buckets.length; i++) {
      insertionSort(buckets[i]);
    }

    int index = 0;
    for (int i = 3; i > -1; i--) {
      for (int j = 0; j < buckets[i].size(); j++) {
        eventArray[index] = buckets[i].get(j);
        index++;
      }
    }
    return new ArrayList<>(
        Arrays.asList(Arrays.copyOfRange(eventArray, 0, Math.min(5, eventArray.length))));
  }
}
