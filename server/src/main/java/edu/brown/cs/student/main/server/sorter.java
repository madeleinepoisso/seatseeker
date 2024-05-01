package edu.brown.cs.student.main.server;


import edu.brown.cs.student.main.server.ticket;
import java.util.ArrayList;
import java.util.List

public class EventSorter {

    public static void insertionSort(List<Item> bucket) {
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

    public static ArrayList bucketSort(float[] arr) {
        int n = arr.length;

        List<Float>[] buckets = new ArrayList[4];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            int val = 0
            if (arr[i].data) {
                val++;
            }
            if (arr[i].time) {
                val++;
            }
            if (arr[i].city) {
                val++;
            }
            buckets[val].add(arr[i]);
        }

        for (int i = 0; i < buckets.length; i++) {
            insertionSort(buckets[i]);
        }

        int index = 0;
        for (int i = 0; i < buckets.length; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                arr[index] = buckets[i].get(j);
                index++;
            }
        }
        return new ArrayList<>(Arrays.asList(Arrays.copyOfRange(arr, 0, 5)));
    }

