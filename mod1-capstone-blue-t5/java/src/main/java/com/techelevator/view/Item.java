package com.techelevator.view;

import java.math.BigDecimal;

public abstract class Item {
    private String name;
    private BigDecimal price;
    private String slot;
    private int quantity;
    private final int startingQuantity = 5;


    public Item(String name, BigDecimal price, String slot) {
        this.name = name;
        this.price = price;
        this.slot = slot;
        this.quantity = startingQuantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSlot() {
        return slot;
    }

    public String getQuantity() {
        String stringQuantity = "";
        if (quantity == 0) {
            stringQuantity = "SOLD OUT";
        } else {
            stringQuantity = String.valueOf(quantity);
        }
        return stringQuantity;
    }

    public void adjustQuantities() {
        if (quantity > 0) {
            quantity --;
        }
    }

}
