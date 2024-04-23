package edu.brown.cs.student.main.server;

public class Ticket {
    public Integer price;
    public String date;
    public String name;
    public String link;
    public Ticket(Integer price, String date, String name, String link) {
        this.price = price;
        this.date = date;
        this.name = name;
        this.link = link;
        //city
        //seat and row
    }
    public String toJSON() {
        return "working on it";
    }
    public String toString() {
        return this.name + " price: " + this.price + " date: " + this.date + " link: " + this.link;
    }
}
