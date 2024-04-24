package edu.brown.cs.student.main.server.ticketScraper;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class DateConverter {
    public static String convertToMilitaryTime(String timeString) {
        String[] parts = timeString.toLowerCase().split(":|(?<=\\d)(?=\\D)");

        // Parse hours and minutes
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // Adjust hours for PM time
        if (parts[2].equals("pm")) {
            hours = (hours == 12) ? 12 : hours + 12;
        } else if (parts[2].equals("am") && hours == 12) {
            hours = 0; // Midnight (12 AM) is 0 in military time
        }

        // Format the time in military time (24-hour clock)
        return String.format("%02d:%02d", hours, minutes);
    }
    public static String convertSpace(String timeString){
        // Split the time string into hours, minutes, and period (am/pm)
        String[] parts = timeString.split("[:\\s]+");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // Adjust hours for PM time
        if (parts[2].equalsIgnoreCase("pm")) {
            hours = (hours == 12) ? 12 : hours + 12;
        } else if (parts[2].equalsIgnoreCase("am") && hours == 12) {
            hours = 0; // Midnight (12 AM) is 0 in military time
        }

        // Format the time in military time (24-hour clock)
        return String.format("%02d:%02d", hours, minutes);
    }
}
