package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.Map;

public class Utils {

  /**
   * Converts a map to JSON string using Moshi library.
   *
   * @param map The map to be converted to JSON.
   * @return The JSON string representation of the map.
   */
  public static String toMoshiJson(Map<String, Object> map) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    String json = adapter.toJson(map);
    System.out.println("map in utils");
    System.out.println(json);
    return json;
  }
}
