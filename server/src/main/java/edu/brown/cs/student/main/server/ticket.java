package edu.brown.cs.student.main.server;

public class ticket {
  public Integer price;
  public String date;
  public String name;
  public String link;
  public String time;
  public String city;
  public String seat;

  public ticket(
      Integer price, String date, String name, String link, String time, String city, String seat) {
    this.price = price;
    this.date = date;
    this.name = name;
    this.link = link;
    this.time = time;
    this.city = city;
    this.seat = seat;

    // city
    // seat and row
  }

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
