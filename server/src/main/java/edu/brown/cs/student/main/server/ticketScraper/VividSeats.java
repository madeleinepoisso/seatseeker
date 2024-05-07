package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.Ticket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class VividSeats implements Scraper {
    /**
     * This map goes from full month to abbreviated in three letters month.
     */
    static final Map<String,String> fullToAbbrev;
    static {
        fullToAbbrev = new HashMap<>();
        fullToAbbrev.put("June","Jun");
        fullToAbbrev.put("August","Aug");
        fullToAbbrev.put("May","May");
        fullToAbbrev.put("April","Apr");
        fullToAbbrev.put("July","Jul");
        fullToAbbrev.put("October","Oct");
        fullToAbbrev.put("November","Nov");
        fullToAbbrev.put("December","Dec");
        fullToAbbrev.put("January","Jan");
        fullToAbbrev.put("February","Feb");
        fullToAbbrev.put("March","Mar");
        fullToAbbrev.put("September","Sep");
    }

    /**
     * The best method returns the best tickets for the query limited to approx 20
     * @param query
     * @return
     */
  @Override
  public List<Ticket> best(String query) {
    List<Ticket> t = this.getInfoGivenQuery(query);
    if (t.size()>0){
        this.setPriceAndSeat(t.get(0));
    }
    System.out.println(t);
    return t;
  }

    /**
     * This helper resets the cookies to prevent bot detection
     */
    private void resetCookies() {
        // Set Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Optional: run in headless mode

        // Set system property
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Create WebDriver instance
        WebDriver driver = new ChromeDriver(options);

        // Load a blank page to ensure cookies are cleared for the current domain
        driver.get("about:blank");

        // Delete all cookies
        driver.manage().deleteAllCookies();

        // Quit WebDriver
        driver.quit();
    }

    /**
     * This function sets the price and seat for a ticket given its link. If we can't laod the website than we
     * simply don't change anything as a baseline price has already been added. Usually only works for the first
     * ticket in the list.
     * @param t
     */
  private void setPriceAndSeat(Ticket t) {
            resetCookies();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run in headless mode
            options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"); // Set user-agent
            WebDriver driver = new ChromeDriver();
            try{
            driver.get(t.link);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String html = driver.getPageSource();
            Document document = Jsoup.parse(html);
            Element listingPrice = document.selectFirst("html");
            String price = listingPrice.selectFirst("span[data-testid=listing-price]").html();
            Element sectionDiv = listingPrice.selectFirst("div[class=styles_box__QqP94 styles_align-center__2ETCO styles_display-flex__3lYqk]");
            String section = sectionDiv.selectFirst("div").text();
            String row = listingPrice.selectFirst("span[data-testid=row]").text();
            t.price = Integer.valueOf(price.replace("$", ""));
            t.seat = "Section " + section + " " + row;
            driver.quit();
        } catch (Exception e) {
            driver.quit();
        }
  }

    /**
     * This is called to get the info given a query. It can set everything except for the seat. The price might
     * not be found or could be innacurate but so far it's been working pretty decently.
     * @param query
     * @return
     */
  public List<Ticket> getInfoGivenQuery(String query) {
    List<Ticket> tickets = new ArrayList<Ticket>();
    Document doc = null;
    String fullQuery = "https://www.vividseats.com/search?searchTerm=" + query;
    try {
      doc =
          Jsoup.connect(fullQuery)
              .referrer("https://www.google.com/")
              .userAgent(
                  "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
              .get();
    } catch (Exception e) {
        System.out.println(e);
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

        Integer minPrice = Integer.parseInt(ticketNode.get("minPrice").asText());
        ZonedDateTime zonedTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        String month = zonedTime.getMonth().toString();
        String day = String.valueOf(zonedTime.getDayOfMonth());
        if (day.length() < 2){
            day = "0"+day;
        }
        date = fullToAbbrev.get(month.charAt(0) + month.substring(1).toLowerCase()) + " " + day;
        String time =
            String.valueOf(zonedTime.getHour()) + ":" + String.valueOf(zonedTime.getMinute());
        Ticket t = new Ticket(minPrice, date, name, link, time, location, "unknown");//we see that it sets everything but the seat
        tickets.add(t);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
    return tickets;
  }

    /**
     * I just use this for testing
     * @param args
     */
  public static void main(String[] args) {
    Scraper vividSeats = new VividSeats();
    vividSeats.best("inter%20miami");
  }
}
