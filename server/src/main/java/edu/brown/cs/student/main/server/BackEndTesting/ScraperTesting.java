package edu.brown.cs.student.main.server.BackEndTesting;

import edu.brown.cs.student.main.server.Event;
import edu.brown.cs.student.main.server.Ticket;
import edu.brown.cs.student.main.server.ticketScraper.Scraper;
import edu.brown.cs.student.main.server.ticketScraper.Stubhub;
import edu.brown.cs.student.main.server.ticketScraper.SeatGeek;
import edu.brown.cs.student.main.server.ticketScraper.VividSeats;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ScraperTesting {
    private Scraper stubHub;
    private Scraper vividSeats;
    private Scraper seatGeek;

    /**
     * Sets up all three functional scrapers before running each test
     */
    @BeforeEach
    public void setup(){
        this.stubHub = new Stubhub();
        this.vividSeats = new VividSeats();
        this.seatGeek = new SeatGeek();
    }

    /**
     * This test stimply tests the edge case in which the query is just nothing
     */
    @Test
    public void testEmptyQuery(){
        List<Ticket> shTix = this.stubHub.best("");
        List<Ticket> vsTix = this.vividSeats.best("");
        List<Ticket> sgTix = this.seatGeek.best("");
        //this test simply proves that no error is thrown when querying something that is empty
    }

    /**
     * This just tests that the first returned is the same for all the major ticket-sellers and also
     * that I am returning the correct things for each scraper.
     */
    @Test
    public void standardTest() {
        List<Ticket> shTix = this.stubHub.best("Boston+Celtics");
        List<Ticket> vsTix = this.vividSeats.best("Boston+Celtics");
        List<Ticket> sgTix = this.seatGeek.best("Boston+Celtics");
        assertTrue(shTix.get(0).city.equals(vsTix.get(0).city) && shTix.get(0).city.equals(sgTix.get(0).city));
        assertTrue(shTix.get(0).date.equals(vsTix.get(0).date) && shTix.get(0).date.equals(sgTix.get(0).date));
    }

    /**
     * Test that the output when calling the stubhub scraper is in the correct format.
     */
    @Test
    public void testStubHubOutput() {
        List<Ticket> tix = this.stubHub.best("argentina+vs+chile");
        for (int i =0; i< tix.size();i++) {
            assertTrue(correctCityFormat(tix.get(i).city));
            assertTrue(correctDateFormat(tix.get(i).date));
            assertTrue(correctTimeFormat(tix.get(i).time));
        }
    }

    /**
     * Test tha the output when calling the seatGeek scraper is in the correct format.
     */
    @Test
    public void testSeatGeekOutput() {
        List<Ticket> tix = this.seatGeek.best("argentina+vs+chile");
        for (int i =0; i< tix.size();i++) {
            assertTrue(correctCityFormat(tix.get(i).city));
            assertTrue(correctDateFormat(tix.get(i).date));
            assertTrue(correctTimeFormat(tix.get(i).time));
        }
    }

    /**
     * Test that the output when calling the vividSeats scraper is in the correct format.
     */
    @Test
    public void testVividSeatsOutput() {
        List<Ticket> tix = this.vividSeats.best("argentina+vs+chile");
        for (int i =0; i< tix.size();i++) {
            assertTrue(correctCityFormat(tix.get(i).city));
            assertTrue(correctDateFormat(tix.get(i).date));
            assertTrue(correctTimeFormat(tix.get(i).time));
        }
    }

    /**
     * Helper function that ensures that the inputted time is in the correct format.
     * @param time should hopefully be in military time
     * @return boolean of whether correct format or not
     */
    private boolean correctTimeFormat(String time) {
        if (time.equals("TBD") || time.equals("tbd")) {
            return true;
        }
        if (time.length() == 5) {
            return true;
        } else {
            System.out.println("incorrect time: " + time);
            return false;
        }
    }

    /**
     *
     * @param date date as a string should be month abbreviation and then day of month
     * @return correct format or not
     */
    private boolean correctDateFormat(String date){
        if (date.equals("TBD") || date.equals("tbd")) {
            return true;
        } else {
            String[] split = date.split(" ");
            if (split[0].length() != 3) {
                return false;
            }
            if (split[1].length() != 2) {
                System.out.println("too short day: " + split[1]);
                return false;
            }
        }
        return true;
    }

    /**
     * Ensures that the city is in format City, (state abbrev)
     * @param location where the event is located
     * @return whether correct format or not
     */
    private boolean correctCityFormat(String location) {
        String[] split = location.split(" ");
        if (split.length != 2){
            return false;
        } else {
            if (split[2].length() != 2) {
                return false;
            }
        }
        return true;
    }
}
