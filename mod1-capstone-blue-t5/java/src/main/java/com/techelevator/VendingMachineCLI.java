package com.techelevator;

import com.techelevator.view.Menu;
import com.techelevator.view.VendingMachine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION};

	Scanner userInput = new Scanner(System.in);

	private Menu menu;

	public VendingMachine vendingMachine;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {

		printWelcomeMessage();

		String fileName = userInput.nextLine();

		vendingMachine = new VendingMachine(verifyInputFileIsValid(fileName));

		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			boolean stayInPurchaseMenu = true;

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				System.out.println(vendingMachine.getAllItems());
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
				while (stayInPurchaseMenu) {
					if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
						System.out.println("Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
						System.out.println("Enter a bill to make a purchase: ");
						String billEnteredString = userInput.nextLine();
						while (!isValidBill(billEnteredString)) {
							System.out.println("That's not a valid bill amount. Please enter $1, $2, $5, $10, or $20: ");
							billEnteredString = userInput.nextLine();
						}

						printCurrentTime();
						printToLog(String.format("   %-24s ", "FEED MONEY"));
						printToLog(String.format("\\%-6s", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));

						feedMoney(billEnteredString);

						printToLog(String.format("\\%-6s \n", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));

						System.out.println("Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));

						choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
						System.out.println("\n" + vendingMachine.getAllItems() + "\nPlease choose your item for purchase: \n");
						System.out.println("Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));

						String itemToPurchase = userInput.nextLine();

						if (vendingMachine.itemExists(itemToPurchase)) {
							if (vendingMachine.itemIsInStock(itemToPurchase)) {
								if (vendingMachine.getBalance().subtract(vendingMachine.getPriceBySlotId(itemToPurchase)).compareTo(BigDecimal.valueOf(0)) == 0 ||
										vendingMachine.getBalance().subtract(vendingMachine.getPriceBySlotId(itemToPurchase)).compareTo(BigDecimal.valueOf(0)) == 1) {
									printCurrentTime();
									printToLog(String.format("   %-24s ", vendingMachine.getNameBySlotId(itemToPurchase) + " " + itemToPurchase));
									printToLog(String.format("\\%-6s", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));

									vendItem(itemToPurchase);

									printToLog(String.format("\\%-6s \n", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));
									System.out.println("Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
								} else {
									System.out.println("Your balance is not enough to purchase the requested item.\n" + "Insert more money or choose an item of lesser value.\n" +
											"Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
								}
							} else {
								System.out.println("The item you selected is not in stock. \n" +
										"Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
							}
						} else {
							System.out.println("Item does not exist.\n" +
									"Your balance is: " + NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance()));
						}

						choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
					} else if (choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
						printCurrentTime();
						printToLog(String.format("   %-24s ", "GIVE CHANGE"));
						printToLog(String.format("\\%1$-6s", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));

						int[] changeDue = finishTransaction();

						System.out.println("\nDispensing " + changeDue[0] + " quarter(s), " + changeDue[1] + " dime(s), and " + changeDue[2] + " nickel(s).\n" +
								"Your balance is: $" + vendingMachine.getBalance());

						printToLog(String.format("\\%-6s \n", NumberFormat.getCurrencyInstance().format(vendingMachine.getBalance())));

						stayInPurchaseMenu = false;
					}
				}
			} else {
				printToLog(">\\`\\`\\` \n");
				break;
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	public boolean isValidBill(String billEntered) {
		boolean isValidBill = false;

		try {
			int billEnteredInt = Integer.parseInt(billEntered);
			if ((billEnteredInt == 1) || (billEnteredInt == 2) || (billEnteredInt == 5) || (billEnteredInt == 10)
					|| (billEnteredInt == 20)) {
					isValidBill = true;
			}
		} catch (Exception e) {
			return isValidBill;
		}

		return isValidBill;
	}

	public void feedMoney(String billEnteredString) {
		int billEnteredInt = Integer.parseInt(billEnteredString);
		vendingMachine.setBalance(vendingMachine.getBalance().add(BigDecimal.valueOf(billEnteredInt)));
	}

	public void vendItem(String itemToPurchase) {
		vendingMachine.dispenseIfInStock(itemToPurchase);
		vendingMachine.setBalance(vendingMachine.getBalance().subtract(vendingMachine.getPriceBySlotId(itemToPurchase)));
	}

	public int[] finishTransaction() {
		int[] changeDueQuartersDimesNickels = new int[3];

		double balanceAsDouble = vendingMachine.getBalance().doubleValue();
		double balanceAsDoubleInPennies = Math.round(balanceAsDouble * 100.00);
		int balanceAsIntInPennies = (int) balanceAsDoubleInPennies;

		int quarters = balanceAsIntInPennies / 25;
		balanceAsIntInPennies -= (quarters * 25);

		int dimes = balanceAsIntInPennies / 10;
		balanceAsIntInPennies -= (dimes * 10);

		int nickels = balanceAsIntInPennies / 5;
		balanceAsIntInPennies -= (nickels * 5);

		vendingMachine.setBalance(BigDecimal.valueOf(balanceAsIntInPennies));

		changeDueQuartersDimesNickels[0] = quarters;
		changeDueQuartersDimesNickels[1] = dimes;
		changeDueQuartersDimesNickels[2] = nickels;

		return changeDueQuartersDimesNickels;

	}

	public File verifyInputFileIsValid(String fileName) {
		File fileToVerify = new File(fileName);
		while (!fileToVerify.exists() && !fileToVerify.canRead()) {
			System.out.println("File path provided is invalid. Please provide a valid file path: ");
			fileToVerify = new File(userInput.nextLine());
		}
		return fileToVerify;
	}

	private void printCurrentTime() {
		LocalDateTime timeStamp = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - hh:mm:ss a");
		String timeStampString = timeStamp.format(formatter);
		printToLog(timeStampString);
	}

	private void printToLog(String logEntry) {
		File logFile = new File("log.txt");
		try(PrintWriter printWriter = new PrintWriter(
				new FileOutputStream(
						logFile, true)
		)) {
			printWriter.append(logEntry);
		} catch (Exception e) {
			System.out.println("Oops! Something went wrong. Please reference: " + e.getMessage());
		}
	}

	private void printWelcomeMessage() {
		System.out.println("            Welcome to the Virtual Vending Machine App            ");
		System.out.println("------------------------------------------------------------------");
		System.out.println("Please provide a file path for the vending machine: \n");
	}

}
