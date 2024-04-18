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
   * Adds a pin with specified details to the Firestore database under the user's collection.
   *
   * @param uid The user ID
   * @param Pinid The pin ID
   * @param longitude The longitude of the pin
   * @param latitude The latitude of the pin
   */
  @Override
  public void addPin(String uid, String Pinid, Double longitude, Double latitude)
      throws IllegalArgumentException {
    if (uid == null || Pinid == null || longitude == null || latitude == null) {
      throw new IllegalArgumentException(
          "addPin: uid, pinid, latitude, or longitude cannot be null");
    }
    Map<String, Object> pinData = new HashMap<>();
    pinData.put("latitude", latitude);
    pinData.put("longitude", longitude);
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference pins = db.collection("users").document(uid).collection("pins");
    pins.document(Pinid).set(pinData);
  }

  /**
   * Retrieves a list of pins associated with the specified user ID from the Firestore database.
   *
   * @param uid The user ID
   * @return List of maps containing pin data.
   */
  @Override
  public List<Map<String, Object>> getCollection(String uid)
      throws InterruptedException, ExecutionException, IllegalArgumentException {
    List<Map<String, Object>> pinsList = new ArrayList<>();
    if (uid == null) {
      throw new IllegalArgumentException("getCollection: uid cannot be null");
    }
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference pins = db.collection("users").document(uid).collection("pins");
    try {
      pins.get()
          .get()
          .forEach(
              document -> {
                Map<String, Object> pinData = new HashMap<>();
                pinData.putAll(document.getData());
                pinsList.add(pinData);
              });
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    return pinsList;
  }

  /**
   * Clears all pins associated with the specified user ID from the Firestore database.
   *
   * @param uid The user ID
   */
  @Override
  public void clearUser(String uid) throws IllegalArgumentException {
    if (uid == null) {
      throw new IllegalArgumentException("removeUser: uid cannot be null");
    }
    Firestore db = FirestoreClient.getFirestore();
    CollectionReference pins = db.collection("users").document(uid).collection("pins");
    pins.listDocuments().forEach(document -> document.delete());
  }
}
