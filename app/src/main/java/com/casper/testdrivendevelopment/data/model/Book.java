package com.casper.testdrivendevelopment.data.model;

import java.io.Serializable;

/**
 * Created by jszx on 2019/9/24.
 */

public class Book implements Serializable{
    private String title;
    private double price;
    private int coverResourceId;

    public Book(String title, double price,int coverResourceId) {
        this.title = title;
        this.price=price;
        this.coverResourceId = coverResourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCoverResourceId() {
        return coverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        this.coverResourceId = coverResourceId;
    }
}
