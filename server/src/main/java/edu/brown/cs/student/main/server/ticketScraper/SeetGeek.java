package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.Ticket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeetGeek implements scraper{

    @Override
    public Ticket best(String query) {
        this.getInfoGivenQuery(query);
        return null;
    }

    @Override
    public List<Ticket> top5(String query) {
        return null;
    }
    public List<Ticket> getInfoGivenQuery(String query) {
        Document doc = null;
        String fullQuery = "https://seatgeek.com/search?f=1&search=" +query+"&ui_origin=home_search";
        String otherQuery = "https://seatgeek.com/copa-america-tickets/international-soccer/2024-06-25-8-30-pm/6308596";
        System.out.println(fullQuery);
        try {
            doc = Jsoup.connect(fullQuery).referrer("https://seatgeek.com/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36").get();
        } catch (IOException e) {
            System.out.println("not working");
        }
        System.out.println(doc);
        return new ArrayList<Ticket>();
    }
    public static void main(String[] args) {
        scraper stubhub = new SeetGeek();
        stubhub.best("argentina%20vs%20chile");
    }
}
