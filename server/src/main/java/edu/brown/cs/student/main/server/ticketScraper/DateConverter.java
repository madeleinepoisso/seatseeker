package edu.brown.cs.student.main.server.ticketScraper;

/**
 * This class contains helper functions which allow the conversion of strings with different formats to military time
 * also in string format
 */
public class DateConverter {
    /**
     * Used for seat geek
     * @param timeString
     * @return
     */
    public static String convertToMilitaryTime(String timeString) {
        if (timeString.equals("tbd") || timeString.equals("TBD")) {
            return "TBD";
        }
        String[] parts = timeString.toLowerCase().split(":|(?<=\\d)(?=\\D)");
        parts[2] = parts[2].replace(" ","");
        // Parse hours and minutes
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        // Adjust hours for PM time
        if (parts[2].equals("pm")) {
            System.out.println("pm");
            hours = (hours == 12) ? 12 : hours + 12;
        } else if (parts[2].equals("am") && hours == 12) {
            hours = 0; // Midnight (12 AM) is 0 in military time
        }

        // Format the time in military time (24-hour clock)
        return String.format("%02d:%02d", hours, minutes);
    }

    /**
     * Used for stubhub.
     * @param timeString
     * @return
     */
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
