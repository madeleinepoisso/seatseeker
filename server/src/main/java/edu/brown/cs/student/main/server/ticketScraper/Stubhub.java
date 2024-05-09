package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.Ticket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Stubhub implements Scraper {

  @Override
  public List<Ticket> best(String query) {
    List<Ticket> t = getInfoGivenQuery(query);
    if (t.size()>5){
      System.out.println(t.get(0).name);
      if (this.betterSimilarity(t.get(5).name,query)<0.5){
        t = getInfoGivenQuery(query);
      }
    }

    for (int i = 0; i < t.size(); i++) {
      this.setPriceAndSeat(t.get(i));
    }
    System.out.println(t);
    return t;
  }

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

  private List<Ticket> getInfoFromJSON(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonNode = mapper.readTree(json);
      JsonNode events = jsonNode.get("eventGrids");
      List<Ticket> tickets = new ArrayList<Ticket>();
      Integer numberOfTix = events.get("2").get("items").size();
      for (int i = 0; i < numberOfTix; i++) {
        String link =
            "https://www.stubhub.com" + events.get("2").get("items").get(i).get("url").asText();
        String name = events.get("2").get("items").get(i).get("name").asText();
        String date = events.get("2").get("items").get(i).get("formattedDate").asText();
        String time = events.get("2").get("items").get(i).get("formattedTime").asText();
        time = DateConverter.convertToMilitaryTime(time);
        String city =
            events
                .get("2")
                .get("items")
                .get(i)
                .get("formattedVenueLocation")
                .asText()
                .replace(", USA", "");
        Ticket t = new Ticket(null, date, name, link, time, city, null);
        tickets.add(t);
      }
      return tickets;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }
  public double betterSimilarity(String s1, String s2){
    String requestBody = "{\"text_1\": \""+s1+"\", \"text_2\": \""+s2+"\"}";
    String apiKey = "e99g/95mkS7Feg4vpDxCuQ==9szJCx6LadfZiMsh";
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost request = new HttpPost("https://api.api-ninjas.com/v1/textsimilarity");
      request.setHeader("Content-Type", "application/json");
      request.setHeader("X-Api-Key", apiKey);

      // Set request body
      StringEntity entity = new StringEntity(requestBody);
      request.setEntity(entity);

      // Execute the request
      HttpResponse response = httpClient.execute(request);

      // Parse and print the response
      HttpEntity httpEntity = response.getEntity();
      if (httpEntity != null) {
        String jsonResponse = EntityUtils.toString(httpEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        double sim = jsonNode.get("similarity").asDouble();
        return sim;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return 0;
    }
    return 0.1;
  }
  public static void main(String[] args) {
    List<Event> eventList = new ArrayList<>();
    Stubhub stubhub = new Stubhub();
    System.out.println(stubhub.best("Argentina+vs+chile"));
  }
}
