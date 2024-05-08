package edu.brown.cs.student.main.server;

/** This class represents each ticket. */
public class Ticket {
  public Integer price;
  public String date;
  public String name;
  public String link;
  public String time;
  public String city;
  public String seat;

  public Ticket(
      Integer price, String date, String name, String link, String time, String city, String seat) {
    this.price = price; // price is simply an integer stating what the price is
    this.date = date; // the date is in the format: "Month(only first three letters) day(number)"
    this.name = name; // string in no particular format
    this.link = link; // full url link
    this.time = time; // String with military time format
    this.city = city; // Returns "City-Name, State(Abbreviation)"
    this.seat =
        seat; // Returns unknown if we can't find. If we can find than returns "Section section Row
    // row""
  }

  /**
   * We might need to implement this method when we are trying to return the tickets to the user as
   * a list of events
   *
   * @return
   */
  public String toJSON() {
    return "working on it";
  }

  public String toString() {
    return this.name
        + " price: "
        + this.price
        + " date: "
        + this.date
        + " link: "
        + this.link
        + " time: "
        + this.time
        + " city: "
        + this.city
        + " seat: "
        + this.seat;
  }
}
