package com.techelevator.view;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Drink extends Item {

    public Drink(String name, BigDecimal price, String slot) {
        super(name, price, slot);
    }

    @Override
    public String toString() {
        return "Glug Glug, Yum! \n" +
                "Purchased " + getName() +
                " for " + NumberFormat.getCurrencyInstance().format(getPrice());
    }

}
