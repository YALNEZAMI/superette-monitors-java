package main.products;

import java.sql.Date;
import java.util.Random;

import main.mp.Superette;

public abstract class Product {
    private String name;
    Date date = new Date(System.currentTimeMillis());
    Random random = new Random();
    private int id = random.nextInt() * Superette.NBR_INIT_CLIENTS * Superette.MAX_PRODUCT_BY_CATEGORY_PER_CLIENT
            * Superette.MAX_SIZE_RAYON;

    private double price;

    public String getName() {
        return name + "-" + id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.name + id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
