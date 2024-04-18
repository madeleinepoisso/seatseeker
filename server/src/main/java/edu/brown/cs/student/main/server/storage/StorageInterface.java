package edu.brown.cs.student.main.server.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface StorageInterface {

  void addPin(String uid, String Pinid, Double longitude, Double latitude);

  List<Map<String, Object>> getCollection(String uid)
      throws InterruptedException, ExecutionException;

  void clearUser(String uid) throws InterruptedException, ExecutionException;
}
