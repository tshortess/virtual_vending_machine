package com.techelevator.view;

import com.techelevator.VendingMachineCLI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;


public class VendingMachineCLITest {

    private VendingMachineCLI vendingMachineCLI;
    private Menu menu;

    @Before
    public void setup() {
        menu = new Menu(System.in, System.out);
        vendingMachineCLI = new VendingMachineCLI(menu);
        vendingMachineCLI.vendingMachine = new VendingMachine(new File("vendingmachine.csv"));
    }

    @Test
    public void isValidBill_Valid_Bills_Return_True() {
        boolean expectedBoolean = true;

        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("1"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("2"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("5"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("10"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("20"));
    }

    @Test
    public void isValidBill_Invalid_Bills_Return_False() {
        boolean expectedBoolean = false;

        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("113"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill("wqeonoqwn"));
        Assert.assertEquals(expectedBoolean, vendingMachineCLI.isValidBill(null));
    }


    @Test
    public void feedMoney_Results_In_Vending_Machine_Balance_12_After_2_Bill_And_10_Bill() {
        BigDecimal expectedBigDecimal = BigDecimal.valueOf(12);

        vendingMachineCLI.feedMoney("2");
        vendingMachineCLI.feedMoney("10");

        Assert.assertEquals(expectedBigDecimal, vendingMachineCLI.vendingMachine.getBalance());
    }

    @Test
    public void finishTransaction_Returns_Array_Of_3_Quarters_1_Dime_And_1_Nickel_For_Balance_Of_90_Cents() {
        int[] expectedIntArray = new int[] {3, 1, 1};

        vendingMachineCLI.vendingMachine.setBalance(BigDecimal.valueOf(0.90));

        Assert.assertArrayEquals(expectedIntArray, vendingMachineCLI.finishTransaction());
        Assert.assertEquals(BigDecimal.valueOf(0), vendingMachineCLI.vendingMachine.getBalance());
    }

}
