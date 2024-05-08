package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class Event {
    public String date;
    public String time;
    public String city;
    public String name;
    public List<Ticket> tickets;
    public Event(String date, String time, String city, String name){
        this.date = date;
        this.time = time;
        this.city = city;
        this.name = name;
        this.tickets = new ArrayList<>();
    }

    public boolean ticketAddBoolean(Ticket ticket){
        //if no tickets are in the list then it can be added
        if (this.tickets.isEmpty()){
            return true;
        }
        if (ticketAlreadyAdded(ticket)){
            return false;
        }
        //check if ticket matches up with event
        //ADD ALL THE OTHER STUFF NEEDED TO BE CHECKED BELOW
        if (ticket.date.equals(this.date) && ticket.city.equals(this.city)){
            //compare similarity of names
            if (similarity(this.name, ticket.name) < 0.3){
                return false;
            }
            String[] timeArray = ticket.time.split(":");
            String[] timeArrayActual = this.time.split(":");
            //below is code to give a leeway to starting time as was observed starting time can vary by 30 minutes
            if (timeArray.length == 2 && timeArrayActual.length == 2){
                int hour = Integer.parseInt(timeArray[0]);
                int min = Integer.parseInt(timeArray[1]);
                int actHour = Integer.parseInt(timeArrayActual[0]);
                int actMin = Integer.parseInt(timeArrayActual[1]);
                int upperMin = min + 30;
                int upperHour = hour;
                int lowerMin = min - 30;
                int lowerHour = hour;
                if (upperMin > 60){
                    upperMin = upperMin - 60;
                    upperHour = upperHour + 1;
                }
                if (lowerMin < 0){
                    lowerMin = lowerMin + 60;
                    lowerHour = lowerHour - 1;
                }
                if (lowerHour == actHour && actMin >= lowerMin){
                    return true;
                } else if (upperHour == actHour && actMin <= upperMin) {
                    return true;
                }
            }
            else{
                if(ticket.time.equals(this.time)) {
                    return true;
                }
            }
        }
        //if gets here then ticket doesn't match up
        return false;
    }
    public boolean ticketAlreadyAdded (Ticket ticket){
        //if ticket has already been added to the list then doesn't need to be added
        for (Ticket checkTicket: this.tickets){
            if (checkTicket.toString().equals(ticket.toString())) {
                return true;
            }
        }
        return false;
    }

    //This takes information, calls the Levenshtein edit distance, and uses the returned number to calculate
    // a percentage of how similar the phrases are.
    // see https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        // ensures that the variable longer gets the word of longer length
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        //if both strings don't have a length
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // This is the Levenshtein Edit Distance - an algorithm that calculates and returns the minimum number of edits
    // needed to be made to match the longer phrase to the shorter phrase
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
    public Integer price(){
        //need to fill this in to return the lowest price out of the tickets. should just be a simple for loop.
        //could also store the tickets in the list in the order of a min heap to achieve faster retrieval.
        return 200;
    }
    public String toString(){
        return this.name + ", local: " + this.city + ", date: "+ this.date+ ", time: " + this.time;
    }
    //BELOW IS CODE TO BE ADDED TO WHEREVER WE COMPILING DIFFERENT TICKETING SERVICES
    //will also be assuming we are using a list of events to keep track
    /**
    public void eventUpdate(){
        for (Event event : this.eventList){
            for (Ticket ticket : this.ticketList){
                if(event.ticketAlreadyAdded(ticket)){
                    this.ticketList.remove(ticket);
                    break;
                }
                if(event.ticketAddBoolean(ticket)){
                    this.tickets.add(ticket);
                    this.ticketList.remove(ticket);
                }
                else{
                    Event nameForEvent = new Event();
                    this.eventList.add(nameForEvent);
                }
            }
        }
    }
     **/
}
