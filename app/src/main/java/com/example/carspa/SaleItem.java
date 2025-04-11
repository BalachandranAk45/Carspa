package com.example.carspa;


public class SaleItem {
    private String itemName;
    private int quantity;
    private double rate;

    public SaleItem(String itemName, int quantity, double rate) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.rate = rate;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getRate() {
        return rate;
    }
}

