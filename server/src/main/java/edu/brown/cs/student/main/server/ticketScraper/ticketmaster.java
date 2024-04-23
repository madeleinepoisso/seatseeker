package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.Ticket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ticketmaster implements scraper {

  @Override
  public List<Ticket> best(String query) {
    String link = getLinksGivenQuery(query).get(0);
    System.out.println(link);
    Document doc = null;
    try {
      doc =
          Jsoup.connect(link)
              .referrer("https://www.ticketmaster.com/")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
              .get();
    } catch (IOException e) {
      System.out.println(e);
    }
    Elements linkElements = doc.select("li[data-index=qp-0]");
    System.out.println(linkElements);
    return null;
  }

  public List<String> getLinksGivenQuery(String query) {
    Document doc = null;
    try {
      doc = Jsoup.connect("https://www.ticketmaster.com/search?q=" + query).get();
    } catch (IOException e) {
      System.out.println("issue searching");
    }
    Elements linkElements = doc.select("a[data-testid=event-list-link]");
    List<String> textList = new ArrayList<>();

    // Iterate over the elements using a regular for loop
    for (int i = 0; i < linkElements.size(); i++) {
      Element element = linkElements.get(i);
      String link = element.attr("href");
      textList.add(link);
    }
    return textList;
  }

  public static void main(String[] args) {
    scraper ticketmaster = new ticketmaster();
    ticketmaster.best("argentina%20vs%20chile");
  }
}
