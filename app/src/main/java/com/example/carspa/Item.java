package com.example.carspa;

public class Item {
    private String name;
    private int quantity;
    private double rate;

    public Item(String name, int quantity, double rate) {
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRate() {
        return rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
