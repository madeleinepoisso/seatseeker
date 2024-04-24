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

/**
 * This scraper actually fully works!!!!!!
 */
public class Stubhub implements Scraper {
  /**
   * This method returns a list of the best tickets. Limited to 20, but will result in as many are returned given
   * the query.
   * @param query any input given by
   * @return
   */
  @Override
  public List<Ticket> best(String query) {
    List<Ticket> t = getInfoGivenQuery(query);
    for (int i = 0; i < t.size(); i++) {//loops through all the tickets that have everything but prices and seats
      this.setPriceAndSeat(t.get(i));
    }
    System.out.println(t);
    return t;
  }

  /**
   * This gets info given query excluding the seat and price. It gets the appropriate json and then
   * uses the helper method to use that json to return the appropriate tickets.
   * @param query
   * @return
   */
  public List<Ticket> getInfoGivenQuery(String query) {
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
    List<Ticket> tickets = getInfoFromJSON(json);
    return tickets;
  }

  /**
   * This helper function sets the seat and price of the ticket given the link already in the
   * ticket.
   *
   * @param t
   */
  private void setPriceAndSeat(Ticket t) {
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
        String priceWFees = node.get("grid").get("items").get(i).get("price").asText();
        if (node.get("grid").get("items").get(i).get("availableTickets").asInt() == 0
            || node.get("grid").get("items").get(i).has("sectionMapName") == false
            || node.get("grid").get("items").get(i).has("row") == false) {
          i++;
        } else {
          t.price = Integer.parseInt(priceWFees.replaceAll("[^0-9]", ""));
          t.seat =
              "Section "
                  + node.get("grid").get("items").get(i).get("sectionMapName").asText()
                  + " Row "
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

  /**
   * This function takes the json with all the info and parses it and gets the relevant information
   * @param json a string representing a json
   * @return
   */
  private List<Ticket> getInfoFromJSON(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(json);
      JsonNode events = jsonNode.get("eventGrids");
      List<Ticket> tickets = new ArrayList<Ticket>();
      Integer numberOfTix = events.get("2").get("items").size();
      for (int i = 0; i < numberOfTix; i++) {
        //get all the different properties from the place where the tickets are stored.
        String link =
            "https://www.stubhub.com" + events.get("2").get("items").get(i).get("url").asText();
        String name = events.get("2").get("items").get(i).get("name").asText();
        String date = events.get("2").get("items").get(i).get("formattedDate").asText();
        String time = DateConverter.convertSpace(events.get("2").get("items").get(i).get("formattedTime").asText());
        String city = events.get("2").get("items").get(i).get("formattedVenueLocation").asText().replace(", USA","");
        Ticket t = new Ticket(null, date, name, link, time, city, null);
        tickets.add(t);
      }
      return tickets;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }

  /**
   * I've just been using main to test, we don't actually need it.
   * @param args
   */
  public static void main(String[] args) {
    Scraper stubhub = new Stubhub();
    stubhub.best("argentina%20vs%20Chile");
  }
}
