package banking;

import java.util.Scanner;

public class Card {

    private Scanner scanner;
    private final Utils utils;
    private final CardJDBC cardJDBC;
    private String curCardNum;

    public Card(Utils utils, CardJDBC cardJDBC) {
        this.utils = utils;
        this.cardJDBC = cardJDBC;
    }

    public void mainMenu() {
        System.out.println();
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        cardJDBC.createNewDatabase();
        scanner = new Scanner(System.in);
        switch (scanner.next()) {
            case "1":
                newAccount();
                mainMenu();
                break;
            case "2":
                logIn();
                break;
            case "0":
                System.out.println("Bye!");
                break;
        }
    }

    public void logIn() {
        scanner = new Scanner(System.in);

        System.out.println("Enter your card number: ");
        curCardNum = scanner.next();
        if (curCardNum.equals("0")) {
            scanner.close();
            System.out.println("Bye!");
        } else {
            System.out.println("Enter your PIN: ");
            String curPin = scanner.next();
            if (curPin.equals("0")) {
                scanner.close();
                System.out.println("Bye!");
            } else {
                if (!cardJDBC.findCurrentData(curCardNum, curPin)) {
                    System.out.println("Wrong card number or PIN!");
                    logIn();
                } else {
                    System.out.println("You have successfully logged in!");
                    accountMenu();
                }
            }
        }
    }

    public void accountMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");

        scanner = new Scanner(System.in);
        switch (scanner.next()) {
            case "1":
                System.out.println("Balance: " + cardJDBC.getBalance(curCardNum));
                accountMenu();
                break;
            case "2":
                System.out.println("Enter income:");
                int income = scanner.nextInt();
                cardJDBC.addBalance(curCardNum, income);
                System.out.println("Income was added!");
                accountMenu();
                break;
            case "3":
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String transferCard = scanner.next();

                // Receiver's card number the Luhn algorithm
                if (!utils.checkCardForLuhn(transferCard)) {
                    System.out.println("Probably you made a mistake in the card number. Please try again!");
                    accountMenu();
                    break;
                }

                // If the receiver's card number doesn’t exist
                if (cardJDBC.getBalance(transferCard) == null) {
                    System.out.println("Such a card does not exist.");
                    accountMenu();
                    break;
                }

                System.out.println("Enter how much money you want to transfer:");
                int money = scanner.nextInt();

                if (Integer.parseInt(cardJDBC.getBalance(curCardNum)) >= money) {
                    cardJDBC.transfer(curCardNum, cardJDBC.getBalance(curCardNum), transferCard, cardJDBC.getBalance(transferCard), money);
                    System.out.println("Success!");
                } else {
                    System.out.println("Not enough money!");
                }
                accountMenu();
                break;
            case "4":
                cardJDBC.deleteAccount(curCardNum);
                System.out.println("The account has been closed!");
                mainMenu();
                break;
            case "5":
                System.out.println("You have successfully logged out!");
                mainMenu();
                break;
            case "0":
                System.out.println("Bye!");
                break;
        }
    }

    public void newAccount() {
        String cardNumber = utils.generateCard();
        String pin = utils.generatePin();

        cardJDBC.insert(cardNumber, pin);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
    }
}
