package com.techelevator.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Scanner;

public class VendingMachineTest {

    private VendingMachine vendingMachine;

    @Before
    public void setup() {
        vendingMachine = new VendingMachine(new File("vendingmachine.csv"));
    }

    @Test
    public void getAllItems_From_Valid_File_Returns_All_Items_From_File_Fully_Stocked() {
        String expectedString = "";
        try (Scanner readScanner = new Scanner(new File("testFile.txt"))) {
            while (readScanner.hasNextLine()) {
               expectedString += readScanner.nextLine() + " \n";
            }
        } catch (Exception e) {
            System.out.println("Oops, there was an error - see message: " + e.getMessage());
        }

        Assert.assertEquals(expectedString, vendingMachine.getAllItems());
    }

    @Test
    public void getNameBySlotId_Valid_Items_Returns_Item_Name() {
        String expectedString1 = "Potato Crisps";
        String expectedString2 = "Cola";
        String expectedString3 = "Little League Chew";
        String expectedString4 = "Wonka Bar";

        Assert.assertEquals(expectedString1, vendingMachine.getNameBySlotId("A1"));
        Assert.assertEquals(expectedString2, vendingMachine.getNameBySlotId("C1"));
        Assert.assertEquals(expectedString3, vendingMachine.getNameBySlotId("D2"));
        Assert.assertEquals(expectedString4, vendingMachine.getNameBySlotId("B3"));
    }

    @Test
    public void getNameBySlotId_Invalid_Items_Returns_Empty_String() {
        String expectedString = "";

        Assert.assertEquals(expectedString, vendingMachine.getNameBySlotId("Awqenqo"));
        Assert.assertEquals(expectedString, vendingMachine.getNameBySlotId("CC"));
        Assert.assertEquals(expectedString, vendingMachine.getNameBySlotId("D5"));
    }

    @Test
    public void getNameBySlotId_Null_Returns_Empty_String() {
        String expectedString = "";

        Assert.assertEquals(expectedString, vendingMachine.getNameBySlotId(null));
    }

    @Test
    public void getPriceBySlotId_Valid_Items_Returns_Item_Price() {
        BigDecimal expectedBigDecimal1 = BigDecimal.valueOf(3.05);
        BigDecimal expectedBigDecimal2 = BigDecimal.valueOf(1.25);
        BigDecimal expectedBigDecimal3 = BigDecimal.valueOf(0.95);
        BigDecimal expectedBigDecimal4 = BigDecimal.valueOf(1.50);

        Assert.assertEquals(expectedBigDecimal1, vendingMachine.getPriceBySlotId("A1"));
        Assert.assertEquals(expectedBigDecimal2, vendingMachine.getPriceBySlotId("C1"));
        Assert.assertEquals(expectedBigDecimal3, vendingMachine.getPriceBySlotId("D2"));
        Assert.assertEquals(expectedBigDecimal4, vendingMachine.getPriceBySlotId("B3"));
    }

    @Test
    public void getPriceBySlotId_Invalid_Items_Returns_0() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(0);

        Assert.assertEquals(expectedBigDecimal, vendingMachine.getPriceBySlotId("Awqenqo"));
        Assert.assertEquals(expectedBigDecimal, vendingMachine.getPriceBySlotId("CC"));
        Assert.assertEquals(expectedBigDecimal, vendingMachine.getPriceBySlotId("D5"));
    }

    @Test
    public void getPriceBySlotId_Null_Returns_0() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(0);

        Assert.assertEquals(expectedBigDecimal, vendingMachine.getPriceBySlotId(null));
    }

    @Test
    public void getQuantityBySlotId_Valid_Items_Returns_Expected_Item_Quantities() {
        String expectedString1 = "5";
        String expectedString2 = "4";
        String expectedString3 = "2";

        vendingMachine.dispenseIfInStock("B3");

        vendingMachine.dispenseIfInStock("C1");

        vendingMachine.dispenseIfInStock("D2");
        vendingMachine.dispenseIfInStock("D2");
        vendingMachine.dispenseIfInStock("D2");

        Assert.assertEquals(expectedString1, vendingMachine.getQuantityBySlotId("A1"));
        Assert.assertEquals(expectedString2, vendingMachine.getQuantityBySlotId("B3"));
        Assert.assertEquals(expectedString2, vendingMachine.getQuantityBySlotId("C1"));
        Assert.assertEquals(expectedString3, vendingMachine.getQuantityBySlotId("D2"));
    }

    @Test
    public void getQuantityBySlotId_Sold_Out_Item_Returns_Sold_Out() {
        String expectedString = "SOLD OUT";

        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");

        Assert.assertEquals(expectedString, vendingMachine.getQuantityBySlotId("A1"));
    }

    @Test
    public void getQuantityBySlotId_Invalid_Items_Returns_Empty_String() {
        String expectedString = "";

        Assert.assertEquals(expectedString, vendingMachine.getQuantityBySlotId("Awqenqo"));
        Assert.assertEquals(expectedString, vendingMachine.getQuantityBySlotId("CC"));
        Assert.assertEquals(expectedString, vendingMachine.getQuantityBySlotId("D5"));
    }

    @Test
    public void getQuantityBySlotId_Null_Returns_Empty_String() {
        String expectedString = "";

        Assert.assertEquals(expectedString, vendingMachine.getQuantityBySlotId(null));
    }

    @Test
    public void itemIsInStock_Sold_Out_Item_Returns_False() {
        boolean expectedBoolean = false;

        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");
        vendingMachine.dispenseIfInStock("A1");

        Assert.assertEquals(expectedBoolean, vendingMachine.itemIsInStock("A1"));
    }

    @Test
    public void itemIsInStock_In_Stock_Item_Returns_True() {
        boolean expectedBoolean = true;

        vendingMachine.dispenseIfInStock("C1");

        vendingMachine.dispenseIfInStock("D2");
        vendingMachine.dispenseIfInStock("D2");
        vendingMachine.dispenseIfInStock("D2");

        Assert.assertEquals(expectedBoolean, vendingMachine.itemIsInStock("A1"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemIsInStock("B3"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemIsInStock("C1"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemIsInStock("D2"));
    }

    @Test
    public void itemExists_Invalid_Item_Returns_False() {
        boolean expectedBoolean = false;

        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists("Bweop"));
    }

    @Test
    public void itemExists_Null_Item_Returns_False() {
        boolean expectedBoolean = false;

        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists(null));
    }

    @Test
    public void itemExists_Valid_Items_Return_True() {
        boolean expectedBoolean = true;

        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists("A1"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists("B3"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists("C1"));
        Assert.assertEquals(expectedBoolean, vendingMachine.itemExists("D2"));
    }

    @Test
    public void getBalance_Returns_Zero_On_Initialization() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(0);

        Assert.assertEquals(expectedBigDecimal, vendingMachine.getBalance());
    }
}
