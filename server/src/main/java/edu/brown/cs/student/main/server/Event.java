package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String date;
    private String time;
    private String city;
    private String name;
    private List<Ticket> tickets;
    public Event(String date, String time, String city){
        this.date = date;
        this.time = time;
        this.city = city;
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

    //BELOW IS CODE TO BE ADDED TO WHEREVER WE COMPILING DIFFERENT TICKETING SERVICES
    //will also be assuming we are using a list of events to keep track
    /**
    public void eventUpdate(){
        for (Event event : this.eventList){
            for (Ticket ticket : this.ticketList){
                if(event.ticketAlreadyAdded(ticket)){
                    break;
                }
                if(event.ticketAddBoolean(ticket)){
                    this.tickets.add(ticket);
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
