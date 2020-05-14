package fr.weefle.myapplication.Model;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String name;
    private Double price;

    public Transaction(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Transaction(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
