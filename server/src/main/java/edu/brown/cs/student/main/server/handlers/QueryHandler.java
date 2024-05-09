package edu.brown.cs.student.main.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            String query = request.queryParams("query");
            System.out.println(query);
            if(query.contains(" ")) {
                query = query.replace(" ", "%20");
            }
            String cityQuery = request.queryParams("cityQuery");
            System.out.println("cityQuery: " + cityQuery);
            String dateQuery = request.queryParams("dateQuery");
            System.out.println("dateQuery:" + dateQuery);
            String timeQuery = request.queryParams("timeQuery");
            System.out.println("timeQuery: " + timeQuery);
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
            events = sorter.bucketSort(events,dateQuery,timeQuery,cityQuery);
//            System.out.println(events.size());
//            for (Event event : events){
//                System.out.println(event.toString());
//                for (Ticket ticket : event.tickets){
//                    System.out.println(ticket.name);
//                }
//            }
            ObjectMapper mapper = new ObjectMapper();
            String jsonEvents = mapper.writeValueAsString(events);
            responseMap.put("events",jsonEvents);
        } catch (Exception e){
            responseMap.put("response_type","failure");
            e.printStackTrace();
            responseMap.put("error",e.getMessage());
        }
        ObjectMapper mapper = new ObjectMapper();
        String toReturn = mapper.writeValueAsString(responseMap);
        return toReturn;
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
