package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.ticket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class stubhub implements scraper {

  @Override
  public List<ticket> best(String query) {
    List<ticket> t = getInfoGivenQuery(query);
    for (int i = 0; i < t.size(); i++) {
      this.setPriceAndSeat(t.get(i));
    }
    System.out.println(t);
    return t;
  }

  public List<ticket> getInfoGivenQuery(String query) {
    Document doc = null;
    String fullQuery = "https://www.stubhub.com/secure/search?q=" + query + "&sellSearch=false";
    try {
      doc =
          Jsoup.connect(fullQuery)
              .referrer("https://www.stubhub.com/")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
              .get();
    } catch (IOException e) {
      System.out.println("not working");
    }
    Element element = doc.selectFirst("script[id=index-data]");
    String json = element.html();
    List<ticket> tickets = getInfoFromJSON(json);
    return tickets;
  }

  /**
   * This helper function sets the seat and price of the ticket given the link already in the
   * ticket.
   *
   * @param t
   */
  private void setPriceAndSeat(ticket t) {
    Document doc = null;
    JsonNode node = null;
    try {
      doc =
          Jsoup.connect(t.link)
              .referrer("https://www.stubhub.com/")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
              .get();
      Element e = doc.selectFirst("script[id=index-data]");
      String json = e.html();
      ObjectMapper mapper = new ObjectMapper();
      node = mapper.readTree(json);
      Integer amountOfTickets = node.get("grid").get("items").size();
      int i = 0;
      while (i < amountOfTickets) {
        String priceWFees = node.get("grid").get("items").get(i).get("priceWithFees").asText();
        if (node.get("grid").get("items").get(i).get("availableTickets").asInt() == 0
            || node.get("grid").get("items").get(i).has("sectionMapName") == false
            || node.get("grid").get("items").get(i).has("row") == false) {
          i++;
        } else {
          t.price = Integer.parseInt(priceWFees.replaceAll("[^0-9]", ""));
          t.seat =
              "section-"
                  + node.get("grid").get("items").get(i).get("sectionMapName").asText()
                  + " row-"
                  + node.get("grid").get("items").get(i).get("row").asText();
          return;
        }
      }
      t.price = null;
    } catch (Exception e) {
      System.out.println("issues loading ticket info");
      System.out.println(node.get("grid").get("items").get(0));
      t.seat = "N/A";
    }
  }

  private List<ticket> getInfoFromJSON(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(json);
      JsonNode events = jsonNode.get("eventGrids");
      List<ticket> tickets = new ArrayList<ticket>();
      Integer numberOfTix = events.get("2").get("items").size();
      for (int i = 0; i < numberOfTix; i++) {
        String link =
            "https://www.stubhub.com" + events.get("2").get("items").get(i).get("url").asText();
        String name = events.get("2").get("items").get(i).get("name").asText();
        String date = events.get("2").get("items").get(i).get("formattedDate").asText();
        String time = events.get("2").get("items").get(i).get("formattedTime").asText();
        String city = events.get("2").get("items").get(i).get("formattedVenueLocation").asText();
        ticket t = new ticket(null, date, name, link, time, city, null);
        tickets.add(t);
      }
      return tickets;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  public static void main(String[] args) {
    scraper stubhub = new stubhub();
    stubhub.best("argentina%20vs%20Chile");
  }
}
