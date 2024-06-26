package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.Ticket;
import java.util.List;

/**
 * I created this interface to ensure compatability within our backend for the different scrapers of
 * the different ticketing websites. Should be working decently for all except ticketmaster.
 */
public interface Scraper {
  List<Ticket> best(String query);
}
