package edu.brown.cs.student.main.server.BackEndTesting;

import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.Ticket;
import java.util.ArrayList;
import java.util.List;

public class MockedData {

  public static List<Event> createEmptyEventList() {
    return new ArrayList<>();
  }

  public static List<Event> createEventListWithVariousEvents() {
    List<Event> events = new ArrayList<>();

    Event event1 = new Event("Feb 09", "18:00", "Providence, RI", "Taylor Swift Concert");
    event1.tickets.add(new Ticket(100,"Feb 09", "Taylor Swift Concert","link","18:00", "Providence, RI", "22E"));
    events.add(event1);

    Event event2 = new Event("Jun 12", "19:30", "Boulder, CO", "Comedy Show");
    event2.tickets.add(new Ticket(50,"Jun 12", "Comedy Show","link","19:30", "Boulder, CO", "3A"));
    events.add(event2);

    Event event3 = new Event("Dec 27", "10:30", "Boston, MA", "Ballet");
    event3.tickets.add(new Ticket(88,"Dec 27", "Ballet","link","20:30", "Boston, MA", "112J"));
    events.add(event3);

    return events;
  }

  public static List<Event> createEventListWithSameEvent() {
    List<Event> events = new ArrayList<>();

    Event event1 = new Event("Feb 09", "18:00", "Providence, RI", "Taylor Swift Concert");
    event1.tickets.add(new Ticket(100,"Feb 09", "Taylor Swift Concert","link","18:00", "Providence, RI", "22E"));
    events.add(event1);

    Event event2 = new Event("Feb 11", "18:00", "Providence, RI", "Taylor Swift Concert");
    event1.tickets.add(new Ticket(100,"Feb 09", "Taylor Swift Concert","link","18:00", "Providence, RI", "22E"));
    events.add(event1);

    Event event3 = new Event("Feb 09", "18:00", "Orange, CA", "Taylor Swift Concert");
    event1.tickets.add(new Ticket(80,"Feb 09", "Taylor Swift Concert","link","18:00", "Providence, RI", "22E"));
    events.add(event1);

    return events;
  }
}

