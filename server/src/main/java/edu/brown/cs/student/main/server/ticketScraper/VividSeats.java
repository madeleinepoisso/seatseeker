package edu.brown.cs.student.main.server.ticketScraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.brown.cs.student.main.server.ticket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VividSeats implements scraper{

    @Override
    public ticket best(String query) {
        this.getInfoGivenQuery(query);
        return null;
    }

    @Override
    public List<ticket> top5(String query) {
        return null;
    }
    public List<ticket> getInfoGivenQuery(String query) {
        Document doc = null;
        String fullQuery = "https://www.vividseats.com/search?searchTerm=" +query;
        String otherQuery = "";
        System.out.println(fullQuery);
        try {
            doc = Jsoup.connect(fullQuery).referrer("https://vividseats.com").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36").get();
        } catch (IOException e) {
            System.out.println("not working");
        }
        String json = doc.selectFirst("script[id=__NEXT_DATA__]").html();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node =mapper.readTree(json);
//            System.out.println(node);
            JsonNode props = node.get("props");
            JsonNode items = props.get("pageProps").get("initialAllProductionListData").get("items");
            Integer numberOfTix = items.size();
            Integer numberToCount = Integer.max(numberOfTix,5);
            for (int i = 0;i<numberToCount;i++) {
                JsonNode ticketNode = items.get(i);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ArrayList<ticket>();
    }
    public static void main(String[] args) {
        scraper stubhub = new VividSeats();
        stubhub.best("argentina%20vs%20chile");
    }
}
