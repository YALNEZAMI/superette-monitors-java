package main;

import java.sql.Date;
import java.util.Random;

public abstract class Product {
    private String name;
    Date date = new Date(System.currentTimeMillis());
    Random random = new Random();
    private String id = date.toString() + random.nextInt();

    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
