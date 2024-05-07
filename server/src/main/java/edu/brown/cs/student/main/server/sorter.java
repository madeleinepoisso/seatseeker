package edu.brown.cs.student.main.server;

import java.lang.Math.*;
import edu.brown.cs.student.main.server.Ticket;
import edu.brown.cs.student.main.server.Event;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean withinRange(String city, String searchCity) {
        api_url1 = 'https://api.api-ninjas.com/v1/geocoding?city={}'.format(city);
        api_url2 = 'https://api.api-ninjas.com/v1/geocoding?city={}'.format(searchcity);
        response1 = requests.get(api_url1 + city, headers={'JClbpgBLBGqlkM2vQerDbQ==E64E5q17qvCeNtCU'});
        response2 = requests.get(api_url2 + searchCity, headers={'JClbpgBLBGqlkM2vQerDbQ==E64E5q17qvCeNtCU'});
        if (response1.status_code == requests.codes.ok && response2.status_code == requests.codes.ok){
            city_data = response1.json();
            search_city_data = response2.json();

            int city_lat = city_data[0];
            int city_lon = city_data[0];
            int search_city_lat = search_city_data[0];
            int search_city_lon = search_city_data[0];

            int R = 6371.0; 

            int lat1 = city_lat * Math.PI / 180;
            int lon1 = city_lon * Math.PI / 180;
            int lat2 = search_city_lat * Math.PI / 180;
            int lon2 = search_city_lon * Math.PI / 180;

            dlon = lon2 - lon1
            dlat = lat2 - lat1

            a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
            c = 2 * atan2(sqrt(a), sqrt(1 - a))

            distance_km = R * c
            distance_miles = distance_km * 0.621371

            if (distance_miles <= 50){
                return True
            }
            else{
                return False
            }
        }
    else{
        //
        print("Error:", response1.status_code, response1.text);
        print("Error:", response2.status_code, response2.text);
    }


    public static ArrayList bucketSort(List<Event> events, String date, String time, String city) {
        int n = events.length;

        List<Float>[] buckets = new ArrayList[4];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            int val = 0
            if (events[i].date == date ) {
                val++;
            }
            if (events[i].time == time) {
                val++;
            }
            if (withinRange(city, events[i].city)) {
                val++;
            }
            buckets[val].add(events[i]);
        }

        for (int i = 0; i < buckets.length; i++) {
            insertionSort(buckets[i]);
        }

        int index = 0;
        for (int i = 0; i < buckets.length; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                events[index] = buckets[i].get(j);
                index++;
            }
        }
        return new ArrayList<>(Arrays.asList(Arrays.copyOfRange(events, 0, 5)));
    }
}

