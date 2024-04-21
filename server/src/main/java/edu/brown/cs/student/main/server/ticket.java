package edu.brown.cs.student.main.server;

import java.util.Date;

public class ticket {
    public Integer price;
    public String date;
    public String name;
    public String link;
    public ticket(Integer price, String date,String name,String link) {
        this.price = price;
        this.date = date;
        this.name = name;
        this.link = link;
    }
    public String toJSON() {
        return "working on it";
    }
    public String toString() {
        return this.name + " price: " + this.price + " date: " + this.date + " link: " + this.link;
    }
}
