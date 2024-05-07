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
            String[] timeArray = ticket.time.split(":");
            String[] timeArrayActual = this.time.split(":");
            //below is code to give a leeway to starting time as was observed starting time can vary by 30 minutes
            if (timeArray.length == 2){
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
