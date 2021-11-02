package com.techelevator.view;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Candy extends Item{

    public Candy(String name, BigDecimal price, String slot) {
        super(name, price, slot);
    }

    @Override
    public String toString() {
        return "Munch Munch, Yum! \n" +
                "Purchased " + getName() +
                " for " + NumberFormat.getCurrencyInstance().format(getPrice());
    }
}
