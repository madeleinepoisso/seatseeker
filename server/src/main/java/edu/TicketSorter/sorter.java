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

        // 1) Create n empty buckets
        List<Float>[] buckets = new ArrayList[4];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }

        // 2) Put array elements in different buckets
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



















// Driver program to test above function
int main() {
    float arr[] = {0.897, 0.565, 0.656, 0.1234, 0.665, 0.3434};
    int n = sizeof(arr) / sizeof(arr[0]);
    bucketSort(arr, n);

    cout << "Sorted array is \n";
    for (int i = 0; i < n; i++) {
        cout << arr[i] << " ";
    }
    return 0;
}
