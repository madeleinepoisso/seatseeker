package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.ticket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class VividSeats implements scraper {

  @Override
  public List<ticket> best(String query) {
    List<ticket> t = this.getInfoGivenQuery(query);
    for (int i = 0; i < t.size(); i++) {
      System.out.println(t);
      this.setPriceAndSeat(t.get(i));
    }
    return t;
  }

  private void setPriceAndSeat(ticket t) {
      System.setProperty("webdriver.chrome.driver","C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
      WebDriver driver = new ChromeDriver();
      driver.get(t.link);
      try {
          Thread.sleep(5000);
      } catch (InterruptedException e){
          e.printStackTrace();
      }
      String html = driver.getPageSource();
      Document document = Jsoup.parse(html);
      System.out.println(document);
      Element listingPrice = document.selectFirst("html[len=en]");
      String price = listingPrice.html();
      System.out.println(price);
      driver.quit();
  }

  public List<ticket> getInfoGivenQuery(String query) {
    List<ticket> tickets = new ArrayList<ticket>();
    Document doc = null;
    String fullQuery = "https://www.vividseats.com/search?searchTerm=" + query;
    String otherQuery = "";
    System.out.println(fullQuery);
    try {
      doc =
          Jsoup.connect(fullQuery)
              .referrer("https://vividseats.com")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
              .get();
    } catch (Exception e) {
      System.out.println("error getting query");
    }
    String json = doc.selectFirst("script[id=__NEXT_DATA__]").html();
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readTree(json);
      JsonNode props = node.get("props");
      JsonNode pageProps = props.get("pageProps");
      JsonNode items = null;
      if (pageProps.has("initialAllProductionListData")) {
        items = pageProps.get("initialAllProductionListData").get("items");
      } else if (pageProps.has("initialProductionListData")) {
        items = pageProps.get("initialProductionListData").get("items");
      } else {
        return null;
      }
      Integer numberOfTix = items.size();
      for (int i = 0; i < numberOfTix; i++) {
        JsonNode ticketNode = items.get(i);
        String link = "https://vividseats.com" + ticketNode.get("webPath").asText();
        String name = ticketNode.get("name").asText();
        String location =
            ticketNode.get("venue").get("city").asText()
                + ", "
                + ticketNode.get("venue").get("state").asText();
        String date = ticketNode.get("localDate").asText();
        ZonedDateTime zonedTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        String month = zonedTime.getMonth().toString();
        date = month.charAt(0) + month.substring(1).toLowerCase() + " " + zonedTime.getDayOfMonth();
        String time =
            String.valueOf(zonedTime.getHour()) + ":" + String.valueOf(zonedTime.getMinute());
        ticket t = new ticket(null, date, name, link, time, location, null);
        tickets.add(t);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return tickets;
  }

  public static void main(String[] args) {
    scraper stubhub = new VividSeats();
    stubhub.best("argentina%20vs%20Chile");
  }
}
