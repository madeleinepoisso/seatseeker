package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.ticket;

import java.util.List;

/**
 * I created this interface to ensure compatability within our backend for the different scrapers of
 * the different ticketing websites. So far, I've only set up stubhub.
 */
public interface scraper {
    List<ticket> best(String query);
}
