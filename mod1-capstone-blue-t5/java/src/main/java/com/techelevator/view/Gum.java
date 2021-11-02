package com.techelevator.view;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Gum extends Item {
    public Gum(String name, BigDecimal price, String slot) {
        super(name, price, slot);
    }

    @Override
    public String toString() {
        return "Chew Chew, Yum! \n" +
                "Purchased " + getName() +
                " for " + NumberFormat.getCurrencyInstance().format(getPrice());
    }


}
