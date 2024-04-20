package edu.brown.cs.student.main.server;

import java.util.Date;

public class ticket {
    private Integer price;
    private Date date;
    private String name;
    public ticket(Integer price, Date date,String name) {
        this.price = price;
        this.date = date;
        this.name = name;
    }
    public String toJSON() {
        return "working on it";
    }
}
