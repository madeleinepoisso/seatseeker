package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.Ticket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Ticketmaster implements Scraper {

  @Override
  public List<Ticket> best(String query) {
    String link = getLinksGivenQuery(query).get(0);
    System.out.println(link);
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
    Element getData = doc.selectFirst("script[id=__NEXT_DATA__]");
    ObjectMapper mapper = new ObjectMapper();
    JsonNode n;
    try {
      n = mapper.readTree(getData.html());
      JsonNode bla = n.get("props").get("pageProps");
      System.out.println(bla);
    } catch (Exception e) {
      System.out.println("something went wrong reading json");
    }
    // Iterate over the elements using a regular for loop
    for (int i = 0; i < linkElements.size(); i++) {
      Element element = linkElements.get(i);
      String link = element.attr("href");
      textList.add(link);
    }
    return textList;
  }

  public static void main(String[] args) {
    Scraper ticketmaster = new Ticketmaster();
    ticketmaster.best("boston%20celtics");
  }
}
