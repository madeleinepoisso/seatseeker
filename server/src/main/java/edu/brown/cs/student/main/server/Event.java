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
        if (ticket.date.equals(this.date)){
            return true;
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
