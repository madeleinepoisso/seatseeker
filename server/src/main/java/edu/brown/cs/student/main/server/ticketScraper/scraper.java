package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.ticket;

import java.util.List;

public interface scraper {
    ticket best(String query);
    List<ticket> top5(String query);
    List<String> getLinksGivenQuery(String query);
}
