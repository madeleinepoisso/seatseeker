package edu.brown.cs.student.main.server.BackEndTesting;

import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.Ticket;
import edu.brown.cs.student.main.server.handlers.QueryHandler;
import edu.brown.cs.student.main.server.ticketScraper.Scraper;
import edu.brown.cs.student.main.server.ticketScraper.Stubhub;
import edu.brown.cs.student.main.server.ticketScraper.SeatGeek;
import edu.brown.cs.student.main.server.ticketScraper.VividSeats;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTesting {

    //Below method is the method used in the QueryHandler class to actually sort the events
    private void eventUpdate(List<Ticket> ticketList, List<Event> eventList) {
        int checker;

        for (Ticket ticket : ticketList) {
            checker = 0;
            for (Event event : eventList) {
                if (event.ticketAlreadyAdded(ticket)) {
                    checker = 1;
                    break;
                } else if (event.ticketAddBoolean(ticket)) {
                    checker = 1;
                    event.tickets.add(ticket);
                    break;
                }
            }
            if (checker != 1) {
                Event nameForEvent = new Event(ticket.date, ticket.time, ticket.city, ticket.name);
                nameForEvent.tickets.add(ticket);
                eventList.add(nameForEvent);
            }
            checker = 0;
        }
    }

    //Below method will be used to see if the events list are the same
    private boolean eventEqual(List<Event> actualEvents, List<Event> checkEvents){
        //if not same number of events return false
        if(actualEvents.size() != checkEvents.size()){
            return false;
        }
        //start to go through events 1 by 1
        for(int i = 0; i<actualEvents.size(); i++){
            //if ticket size doesn't match then return false
            if(actualEvents.get(i).tickets.size() != checkEvents.get(i).tickets.size()){
                return false;
            }
            //go through ticket 1 by 1
            for(int j = 0; j < checkEvents.get(i).tickets.size(); j++){
                //make sure that every ticket in checkEvents is in actualEvents
                //using ticketAlreadyAdded checker as each list should be same so should be 'already added'
                if(!actualEvents.get(i).ticketAlreadyAdded(checkEvents.get(i).tickets.get(j))) {
                    return false;
                }
            }
        }
        //if we get to this point then the lists are the same
        return true;
    }
    @Test
    public void varyingTimesAndNames(){
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks for varying ending to names, varies in time by 30 minutes, and different seat numbers
        Event event1 = new Event("May 10", "18:00", "Boston, MA", "Argentina vs Chile");
        event1.tickets.add(new Ticket(100,"May 10", "Argentina vs Chile","link","18:00", "Boston, MA", "22E"));
        event1.tickets.add(new Ticket(150,"May 10", "Argentina vs Chile Championship","link","18:30", "Boston, MA", "28B"));
        event1.tickets.add(new Ticket(90,"May 10", "Argentina vs Chile Copa America","link","17:30", "Boston, MA", "100C"));
        actualEvents.add(event1);

        //below event checks for varying times at the extreme end of military time (midnight)
        Event event8 = new Event("May 10", "0:00", "Boston, MA", "Burger King");
        event8.tickets.add(new Ticket(100,"May 10", "Burger King","link","0:00", "Boston, MA", "22E"));
        event8.tickets.add(new Ticket(100,"May 10", "Burger King 1","link","23:30", "Boston, MA", "22E"));
        event8.tickets.add(new Ticket(150,"May 10", "Burger King 2","link","0:30", "Boston, MA", "28B"));
        event8.tickets.add(new Ticket(90,"May 10", "Burger King 3","link","0:29", "Boston, MA", "100C"));
        event8.tickets.add(new Ticket(90,"May 10", "Burger King 4","link","23:31", "Boston, MA", "100C"));
        actualEvents.add(event8);

        //event 9 and 10 are to ensure that they are not added to event 8 and that they vary enough in time
        Event event9 = new Event("May 10", "23:29", "Boston, MA", "Burger King");
        event9.tickets.add(new Ticket(100,"May 10", "Burger King","link","23:29", "Boston, MA", "22E"));
        actualEvents.add(event9);

        Event event10 = new Event("May 10", "0:31", "Boston, MA", "Burger King");
        event10.tickets.add(new Ticket(100,"May 10", "Burger King","link","0:31", "Boston, MA", "22E"));
        actualEvents.add(event10);

        actualTickets.add(new Ticket(100,"May 10", "Argentina vs Chile","link","18:00", "Boston, MA", "22E"));
        actualTickets.add(new Ticket(150,"May 10", "Argentina vs Chile Championship","link","18:30", "Boston, MA", "28B"));
        actualTickets.add(new Ticket(90,"May 10", "Argentina vs Chile Copa America","link","17:30", "Boston, MA", "100C"));
        actualTickets.add(new Ticket(100,"May 10", "Burger King","link","0:00", "Boston, MA", "22E"));
        actualTickets.add(new Ticket(100,"May 10", "Burger King 1","link","23:30", "Boston, MA", "22E"));
        actualTickets.add(new Ticket(150,"May 10", "Burger King 2","link","0:30", "Boston, MA", "28B"));
        actualTickets.add(new Ticket(90,"May 10", "Burger King 3","link","0:29", "Boston, MA", "100C"));
        actualTickets.add(new Ticket(90,"May 10", "Burger King 4","link","23:31", "Boston, MA", "100C"));
        actualTickets.add(new Ticket(100,"May 10", "Burger King","link","23:29", "Boston, MA", "22E"));
        actualTickets.add(new Ticket(100,"May 10", "Burger King","link","0:31", "Boston, MA", "22E"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
    @Test
    public void allSameExceptLocation(){
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks if it has same time and date and location but does not have a similar enough name
        Event event6 = new Event("May 10", "1:00", "Boulder, CO", "Oh my golly gosh its the one and only One Direction Performing");
        event6.tickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        event6.tickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        event6.tickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event6);

        //below event checks if it has same time and date and name but does not have the same location
        Event event7 = new Event("May 10", "1:00", "Austin, TX", "Oh my golly gosh its the one and only One Direction Performing");
        event7.tickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Austin, TX", "10A"));
        event7.tickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Austin, TX", "3A"));
        event7.tickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Austin, TX", "15A"));
        actualEvents.add(event7);

        actualTickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Austin, TX", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Austin, TX", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Austin, TX", "15A"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
    @Test
    public void allSameExceptTime(){
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks if it has same name and time and location but has a different date
        Event event4 = new Event("May 10", "19:30", "Boulder, CO", "Comedy Show");
        event4.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        event4.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        event4.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualEvents.add(event4);

        //below event checks if it has same name and date and location but has a different time
        Event event5 = new Event("May 10", "1:00", "Boulder, CO", "Comedy Show");
        event5.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        event5.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        event5.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event5);

        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
    @Test
    public void allSameExceptDate(){
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks for extremes of prices and varies the changes in name
        Event event2 = new Event("Jun 12", "19:30", "Boulder, CO", "Comedy Show");
        event2.tickets.add(new Ticket(50,"Jun 12", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        event2.tickets.add(new Ticket(190000,"Jun 12", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        event2.tickets.add(new Ticket(0,"Jun 12", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualEvents.add(event2);

        //below event checks if it has same name and time and location but has a different date
        Event event4 = new Event("May 10", "19:30", "Boulder, CO", "Comedy Show");
        event4.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        event4.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        event4.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualEvents.add(event4);

        actualTickets.add(new Ticket(50,"Jun 12", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"Jun 12", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"Jun 12", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
    @Test
    public void allSameExceptNameNotSimilarEnough(){
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks if it has same name and date and location but has a different time
        Event event5 = new Event("May 10", "1:00", "Boulder, CO", "Comedy Show");
        event5.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        event5.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        event5.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event5);

        //below event checks if it has same time and date and location but does not have a similar enough name
        Event event6 = new Event("May 10", "1:00", "Boulder, CO", "Oh my golly gosh its the one and only One Direction Performing");
        event6.tickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        event6.tickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        event6.tickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event6);

        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
    @Test
    public void integrationOfVariousTestsIntoOne() {
        List<Event> actualEvents = new ArrayList<>();
        List<Ticket> actualTickets = new ArrayList<>();

        //below event checks for varying ending to names, varies in time by 30 minutes, and different seat numbers
        Event event1 = new Event("May 10", "18:00", "Boston, MA", "Argentina vs Chile");
        event1.tickets.add(new Ticket(100,"May 10", "Argentina vs Chile","link","18:00", "Boston, MA", "22E"));
        event1.tickets.add(new Ticket(150,"May 10", "Argentina vs Chile Championship","link","18:30", "Boston, MA", "28B"));
        event1.tickets.add(new Ticket(90,"May 10", "Argentina vs Chile Copa America","link","17:30", "Boston, MA", "100C"));
        actualEvents.add(event1);

        //below event checks for extremes of prices and varies the changes in name
        Event event2 = new Event("Jun 12", "19:30", "Boulder, CO", "Comedy Show");
        event2.tickets.add(new Ticket(50,"Jun 12", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        event2.tickets.add(new Ticket(190000,"Jun 12", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        event2.tickets.add(new Ticket(0,"Jun 12", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualEvents.add(event2);

        //random event to throw in there to help test
        Event event3 = new Event("Dec 27", "10:30", "Boston, MA", "Ballet");
        event3.tickets.add(new Ticket(88,"Dec 27", "Ballet","link","20:30", "Boston, MA", "112J"));
        actualEvents.add(event3);

        //below event checks if it has same name and time and location but has a different date
        Event event4 = new Event("May 10", "19:30", "Boulder, CO", "Comedy Show");
        event4.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        event4.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        event4.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualEvents.add(event4);

        //below event checks if it has same name and date and location but has a different time
        Event event5 = new Event("May 10", "1:00", "Boulder, CO", "Comedy Show");
        event5.tickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        event5.tickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        event5.tickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event5);

        //below event checks if it has same time and date and location but does not have a similar enough name
        Event event6 = new Event("May 10", "1:00", "Boulder, CO", "Oh my golly gosh its the one and only One Direction Performing");
        event6.tickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        event6.tickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        event6.tickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));
        actualEvents.add(event6);

        //below event checks if it has same time and date and name but does not have the same location
        Event event7 = new Event("May 10", "1:00", "Austin, TX", "Oh my golly gosh its the one and only One Direction Performing");
        event7.tickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Austin, TX", "10A"));
        event7.tickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Austin, TX", "3A"));
        event7.tickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Austin, TX", "15A"));
        actualEvents.add(event7);

        actualTickets.add(new Ticket(100,"May 10", "Argentina vs Chile","link","18:00", "Boston, MA", "22E"));
        actualTickets.add(new Ticket(150,"May 10", "Argentina vs Chile Championship","link","18:30", "Boston, MA", "28B"));
        actualTickets.add(new Ticket(90,"May 10", "Argentina vs Chile Copa America","link","17:30", "Boston, MA", "100C"));
        actualTickets.add(new Ticket(50,"Jun 12", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"Jun 12", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"Jun 12", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(88,"Dec 27", "Ballet","link","20:30", "Boston, MA", "112J"));
        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","19:30", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","19:30", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","19:30", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Great Comedy Show","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Comedy of the Show","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Comedy Show huh?","link","1:00", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Boulder, CO", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Boulder, CO", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Boulder, CO", "15A"));
        actualTickets.add(new Ticket(50,"May 10", "Oh my golly gosh its the one and only One Direction Performing??","link","1:00", "Austin, TX", "10A"));
        actualTickets.add(new Ticket(190000,"May 10", "Oh my golly gosh its the one and only One Direction Performing hubba","link","1:00", "Austin, TX", "3A"));
        actualTickets.add(new Ticket(0,"May 10", "Oh my golly gosh its the one and only One Direction Performing YAYYY","link","1:00", "Austin, TX", "15A"));

        List<Event> checkEvents = new ArrayList<>();
        eventUpdate(actualTickets, checkEvents);
        assertTrue(eventEqual(actualEvents,checkEvents));
    }
}
