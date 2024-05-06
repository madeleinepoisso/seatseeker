package edu.brown.cs.student.main.server.handlers;

import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.EventSorter;
import edu.brown.cs.student.main.server.Ticket;
import edu.brown.cs.student.main.server.ticketScraper.Scraper;
import edu.brown.cs.student.main.server.ticketScraper.SeatGeek;
import edu.brown.cs.student.main.server.ticketScraper.Stubhub;
import edu.brown.cs.student.main.server.ticketScraper.VividSeats;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String,Object> responseMap = new HashMap<>();
        try {
            String query = request.queryParams("query").replace(" ","%20");
            Scraper SH = new Stubhub();
            Scraper SG = new SeatGeek();
            Scraper VS  = new VividSeats();
            /**
             * We should do multithreading here to make this go faster
             * since there's no reason to wait for the prior scrapers
             * to return before running the next ones
             */
            List<Ticket> StubHubTix = SH.best(query);
            List<Ticket> SeatGeekTix = SG.best(query);
            List<Ticket> VividTix = VS.best(query);
            List<Event> events = new ArrayList<>();
            this.eventUpdate(StubHubTix, events);
            this.eventUpdate(SeatGeekTix, events);
            this.eventUpdate(VividTix,events);
            EventSorter sorter = new EventSorter();
            sorter.insertionSort(events);
            responseMap.put("events",events);
        } catch (Exception e){
            responseMap.put("response_type","failure");
            e.printStackTrace();
            responseMap.put("error",e.getMessage());
        }
        System.out.println(responseMap);
        return responseMap;
    }
    private void eventUpdate(List<Ticket> ticketList, List<Event> eventList){
        int checker;

        for (Ticket ticket : ticketList){
            checker = 0;
            for (Event event : eventList){
                if(event.ticketAlreadyAdded(ticket)){
                    checker = 1;
                    break;
                }
                else if(event.ticketAddBoolean(ticket)){
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
}
