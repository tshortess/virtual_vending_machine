package com.techelevator.view;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

    private List<Item> listOfItems;

    private BigDecimal balance = BigDecimal.valueOf(0);

    public VendingMachine(File fileName) {
        listOfItems = itemList(fileName);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getNameBySlotId(String slotID) {
        String nameOfItem = "";
        for (Item item: listOfItems) {
            if (item.getSlot().equals(slotID)) {
                nameOfItem = item.getName();
            }
        }
        return nameOfItem;
    }

    public BigDecimal getPriceBySlotId(String slotID) {
        BigDecimal priceOfItem = BigDecimal.valueOf(0);
        for (Item item: listOfItems) {
            if (item.getSlot().equals(slotID)) {
                priceOfItem = priceOfItem.add(item.getPrice());
            }
        }
        return priceOfItem;
    }

    private List<Item> itemList(File fileName) {
        List<Item> listOfItems = new ArrayList<>();

        try (Scanner readScanner = new Scanner(fileName)) {

            String name = "";
            BigDecimal price;
            String slot = "";

            while (readScanner.hasNextLine()) {
                String itemToAdd = readScanner.nextLine();
                String[] itemToAddArray = itemToAdd.split("\\|");

                name = itemToAddArray[1];

                double priceFromArray = Double.parseDouble(itemToAddArray[2]);
                price = BigDecimal.valueOf(priceFromArray);

                slot = itemToAddArray[0];

                if (itemToAddArray[3].equals("Chip")) {
                    listOfItems.add(new Chip(name, price, slot));
                } else if (itemToAddArray[3].equals("Candy")) {
                    listOfItems.add(new Candy(name, price, slot));
                } else if (itemToAddArray[3].equals("Drink")) {
                    listOfItems.add(new Drink(name, price, slot));
                } else if (itemToAddArray[3].equals("Gum")) {
                    listOfItems.add(new Gum(name, price, slot));
                }
            }
        } catch (Exception e) {
            System.out.println("Oops! Something went wrong.");
            System.out.println("Please restart the vending machine and input a valid vending machine file.");
        }
        return listOfItems;
    }

    public void dispenseIfInStock(String slotID) {
        try {
            if (itemExists(slotID) && itemIsInStock(slotID)) {
                for (Item item : listOfItems) {
                    if (item.getSlot().equals(slotID)) {
                        item.adjustQuantities();
                        System.out.println(item.toString());
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("Oops! Something went wrong. Please reference: " + e.getMessage());
        }
    }

    public boolean itemIsInStock(String slotID) {
        return (getQuantityBySlotId(slotID).equals("SOLD OUT")) ? false : true;
    }

    public boolean itemExists(String slotID) {
        boolean doesItemExist = false;

        for (Item item: listOfItems) {
            if (item.getSlot().equals(slotID)) {
                doesItemExist = true;
            }
        }
        return doesItemExist;
    }

    public String getQuantityBySlotId(String slotID) {
        String quantityOfItem = "";
        for (Item item: listOfItems) {
            if (item.getSlot().equals(slotID)) {
                quantityOfItem = item.getQuantity();
            }
        }
        return quantityOfItem;
    }

    public String getAllItems() {
        String stringOfItems = "";

        for (Item item : listOfItems) {
            stringOfItems += String.format("|%1$2s| %2$-20s %3$6s %4$8s \n", item.getSlot(),
                    item.getName(), NumberFormat.getCurrencyInstance().format(item.getPrice()),
                    "(qty: " + item.getQuantity() + ")");
        }

        return stringOfItems;
    }

}
