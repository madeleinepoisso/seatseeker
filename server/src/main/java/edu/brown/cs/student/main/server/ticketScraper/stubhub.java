package edu.brown.cs.student.main.server.ticketScraper;
import edu.brown.cs.student.main.server.ticket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class stubhub implements scraper{

    @Override
    public ticket best(String query) {
        List<ticket> t = getInfoGivenQuery(query);
        this.setPrice(t.get(0));
        System.out.println(t.get(0));
        return t.get(0);
    }

    @Override
    public List<ticket> top5(String query) {
        return null;
    }

    public List<ticket> getInfoGivenQuery(String query) {
        Document doc = null;
        String fullQuerry = "https://www.stubhub.com/secure/search?q=" +query+"&sellSearch=false";
        try {
            doc = Jsoup.connect("https://www.stubhub.com/secure/search?q=" +query+"&sellSearch=false").referrer("https://www.stubhub.com/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36").get();
        } catch (IOException e) {
            System.out.println("not working");
        }
        Element element = doc.selectFirst("script[id=index-data]");
        String json = element.html();
        List<ticket> tickets = getInfoFromJSON(json);
        return tickets;
    }
    private void setPrice(ticket t) {
        Document doc = null;
        try {
            doc = Jsoup.connect(t.link).referrer("https://www.stubhub.com/").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36").get();
            Element e = doc.selectFirst("script[id=index-data]");
            String json = e.html();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            System.out.println(node.get("grid").get("items").get(0));
            String priceWFees = node.get("grid").get("items").get(0).get("price").asText();
            t.price = Integer.parseInt(priceWFees.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            System.out.println("couldn't find price");
        }
    }
    private List<ticket> getInfoFromJSON(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode events = jsonNode.get("eventGrids");
            List<ticket> tickets = new ArrayList<ticket>();
            Integer numberOfTix = events.get("2").get("items").size();
            for (int i=0;i<numberOfTix;i++) {
                String link = "https://www.stubhub.com"+events.get("2").get("items").get(i).get("url").asText();
                String name = events.get("2").get("items").get(i).get("name").asText();
                String date = events.get("2").get("items").get(i).get("formattedDate").asText();
                ticket t = new ticket(null,date,name,link);
                tickets.add(t);
            }
            return tickets;
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    public static void main(String[] args) {
        scraper stubhub = new stubhub();
        stubhub.best("boston%20celtics");
    }
}
