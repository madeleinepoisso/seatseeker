package edu.brown.cs.student.main.server.ticketScraper;

import edu.brown.cs.student.main.server.ticket;
import java.io.IOException;
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

public class ticketmaster implements scraper {
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
  @Override
  public List<ticket> best(String query) {
    String link = getLinksGivenQuery(query).get(0);
    System.out.println(link);
    resetCookies();
    try {
      Thread.sleep(3000);
    }catch (InterruptedException e){
      e.printStackTrace();
    }
    System.setProperty("webdriver.chrome.driver","C:\\Users\\mclaw\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Run in headless mode
    options.addArguments("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"); // Set user-agent
    WebDriver driver = new ChromeDriver();
    driver.get(link);
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(2000, 4000));
    } catch (InterruptedException e){
      e.printStackTrace();
    }
    String html = driver.getPageSource();
    Document document = Jsoup.parse(html);
    System.out.println(document);
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
    ticketmaster.best("boston%20celtics");
  }
}
