package edu.brown.cs.student.main.server.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseUtilities implements StorageInterface {

  /**
   * Constructs a new FirebaseUtilities object and initializes Firebase App with the provided
   * credentials
   */
  public FirebaseUtilities() throws IOException {

    String workingDirectory = System.getProperty("user.dir");
    Path firebaseConfigPath =
        Paths.get(workingDirectory, "src", "main", "resources", "firebase_config.json");

    FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath.toString());

    FirebaseOptions options =
        new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

    FirebaseApp.initializeApp(options);
  }

  /**
   * Adds an event with specified details to the Firestore database under the user's collection.
   *
   * @param uid - user id
   * @param eventid - number event in user database
   * @param name - name of event
   * @param date - date of event
   * @param time - time of start
   * @param city - location of event
   * @throws IllegalArgumentException
   */
  @Override
  public void addEvent(
      String uid, String eventid, String name, String date, String time, String city)
      throws IllegalArgumentException {
    if (uid == null
        || eventid == null
        || name == null
        || date == null
        || time == null
        || city == null) {
      throw new IllegalArgumentException(
          "addPin: uid, pinid, name, city, time, or date cannot be null");
    }
    Map<String, Object> eventData = new HashMap<>();
    eventData.put("name", name);
    eventData.put("date", date);
    eventData.put("time", time);
    eventData.put("city", city);
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference events = db.collection("users").document(uid).collection("events");
    events.document(eventid).set(eventData);
  }

  /**
   * Retrieves a list of events associated with the specified user ID from the Firestore database.
   *
   * @param uid The user ID
   * @return List of maps containing event data.
   */
  @Override
  public List<Map<String, Object>> getCollection(String uid) throws IllegalArgumentException {
    List<Map<String, Object>> eventsList = new ArrayList<>();
    if (uid == null) {
      throw new IllegalArgumentException("getCollection: uid cannot be null");
    }
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference events = db.collection("users").document(uid).collection("events");
    try {
      events
          .get()
          .get()
          .forEach(
              document -> {
                Map<String, Object> eventData = new HashMap<>();
                eventData.putAll(document.getData());
                eventsList.add(eventData);
              });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return eventsList;
  }

  /**
   * Clears all events associated with the specified user ID from the Firestore database.
   *
   * @param uid The user ID
   */
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference events = db.collection("users").document(uid).collection("events");
    events.listDocuments().forEach(document -> document.delete());
  }
}
