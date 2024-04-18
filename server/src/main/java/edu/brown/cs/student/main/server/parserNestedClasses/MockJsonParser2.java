package edu.brown.cs.student.main.server.parserNestedClasses;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MockJsonParser2 {
  MockGeoJsonObject parsedJSON;

  /**
   * Parses JSON data from a JsonReader and converts it to the specified target type.
   *
   * @param source The JsonReader containing the JSON data.
   * @param targetType The Class representing the target data type to convert the JSON to.
   * @param <T> The generic type of the target data.
   * @return An instance of the target data type parsed from the JSON.
   * @throws IOException if there's an error reading or parsing the JSON data.
   */
  public static <T> T fromJsonGeneral(String source, Class<T> targetType) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<T> adapter = moshi.adapter(targetType);
    //    source.setLenient(true);
    return adapter.fromJson(source);
  }

  public MockGeoJsonObject createGeoJson() throws IOException {
    String filePath = "../client/src/geodata/mockDownload.json";
    try {
      // ***************** READING THE FILE *****************
      FileReader jsonReader = new FileReader(filePath);
      BufferedReader br = new BufferedReader(jsonReader);
      String fileString = "";
      String line = br.readLine();
      while (line != null) {
        fileString = fileString + line;
        line = br.readLine();
      }
      jsonReader.close();

      // ****************** CREATING THE ADAPTER **********

      System.out.println(fileString); // Print JSON string before deserialization
      // this.parsedJSON = fromJsonGeneral(fileString, MockGeoJsonObject.class);
      System.out.println("line 47");

      this.parsedJSON = fromJsonGeneral(fileString, MockGeoJsonObject.class);
      System.out.println(this.parsedJSON);
      return this.parsedJSON;
    } catch (Exception e) {
      System.out.println("error reading" + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    JsonParser2 myparser = new JsonParser2();
    try {
      myparser.createGeoJson();
      System.out.println("finished");

    } catch (IOException e) {
      System.out.println("can not read in json main" + e.getMessage());
    }
  }
}
