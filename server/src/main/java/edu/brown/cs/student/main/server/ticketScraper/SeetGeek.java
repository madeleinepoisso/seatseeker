package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.ticket;
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

public class SeetGeek implements scraper {

  @Override
  public List<ticket> best(String query) {
    this.getInfoGivenQuery(query);
    return null;
  }

  public List<ticket> getInfoGivenQuery(String query) {
    List<ticket> tickets = new ArrayList<>();
    String fullQuery = "https://seatgeek.com/search?f=1&search=" + query + "&ui_origin=home_search";
    System.setProperty("webdriver.chrome.driver","C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Run in headless mode
    options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"); // Set user-agent
    WebDriver driver = new ChromeDriver();
    driver.get(fullQuery);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4000));
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

//      Integer price = Integer.parseInt(data.split("·")[0].replace("$",""));
      tickets.add(new ticket(price,date,name,link,time,city,"Unknown"));
    }
    System.out.println(tickets);
    driver.quit();
    return tickets;
  }

  public static void main(String[] args) {
    scraper stubhub = new SeetGeek();
    stubhub.best("argentina%20vs%20chile");
  }
}
