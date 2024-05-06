package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.Ticket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * The SeatGeek class is a scraper that returns the best tickets given a query. The downside to this scraper is that
 * the seatgeek bot detection is pretty good, so I wasn't able to get the actual seats, but it does get the price pretty
 * reliably.
 */
public class SeatGeek implements Scraper {
  /**
   * This method returns a list of tickets that are sorted in the same way that they appear on the websites
   * @param query whatever the user searched.
   * @return list of tickets
   */
  @Override
  public List<Ticket> best(String query) {
    List<Ticket> tickets = this.getInfoGivenQuery(query);
    System.out.println(tickets);
    return tickets;
  }

  /**
   * This function sets all the info given a query. It does this by using selenium and waiting for the page to
   * load completely before parsing the html and setting the properties of the ticket.
   * @param query
   * @return
   */
  public List<Ticket> getInfoGivenQuery(String query) {
    List<Ticket> tickets = new ArrayList<>();
    String fullQuery = "https://seatgeek.com/search?f=1&search=" + query + "&ui_origin=home_search";
    //The following lines setup selenium so that it isn't detected heavily by the antibot measures.
    System.setProperty("webdriver.chrome.driver","C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");//tells selenium where the driver is in my computer
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Run in headless mode
    options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"); // Set user-agent
    WebDriver driver = new ChromeDriver();
    driver.get(fullQuery);//this actually searches the query.
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4000));//sleeps for a time between 2 and 4s to ensure that the page loads fully.
    } catch (InterruptedException e){
      e.printStackTrace();
    }
    String html = driver.getPageSource();
    Document document = Jsoup.parse(html);
    Element e = document.selectFirst("html");
    Elements links = e.select("li[class=EventItem__EventItemWrapper-sc-1710c790-14 dKHdqo]");
    for (int i=0;i<links.size();i++) {
      String link = links.get(i).selectFirst("a").attr("href");
      String name = links.get(i).selectFirst("p[data-testid=event-item-title]").text();
      String date = links.get(i).selectFirst("p[data-testid=date]").text();
      String time = links.get(i).selectFirst("p[data-testid=time]").text().split(" · ")[1];
      time = DateConverter.convertToMilitaryTime(time);
      Element location = links.get(i).selectFirst("p[data-testid=description]");
      String data = location.text();
      String[] local = data.split("·");
      Integer price = null;
      String city = "N/A";
      if (local.length == 2) {
        city = local[1];
      } else if (local.length ==3) {
        city = local[2];
        price = Integer.parseInt(local[0].replace("$",""));
      }
      tickets.add(new Ticket(price,date,name,link,time,city,"Unknown"));
    }
    driver.quit();
    return tickets;
  }

  public static void main(String[] args) {
    Scraper SeatGeek = new SeatGeek();
    SeatGeek.best("argentina%20vs%20chile");
  }
}
