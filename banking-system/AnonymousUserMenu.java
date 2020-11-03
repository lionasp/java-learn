package banking;

import java.util.Scanner;

public class AnonymousUserMenu implements UserMenu {

    public void printMenu() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    @Override
    public void processOption(int option) {
        Scanner scanner = new Scanner(System.in);
        Account account;
        switch (option) {
            case 1:
                System.out.println("Your card has been created");
                System.out.println("Your card number:");
                // todo: not unique card numbers
                account = Account.generateAccount();
                System.out.println(account.getCardNumber());
                System.out.println("Your card PIN:");
                System.out.println(account.getPinCode());
                DbConnector.addAccount(account);
                break;
            case 2:
                System.out.println("Enter your card number:");
                String cardNumber = scanner.nextLine();
                System.out.println("Enter your PIN:");
                String pinCode = scanner.nextLine();
                account = DbConnector.getAccount(cardNumber, pinCode);
                if (account != null) {
                    System.out.println("You have successfully logged in!");
                    Auth.logIn(account);
                } else {
                    System.out.println("Wrong card number or PIN!");
                }
                break;
            default:
                break;
        }
    }
}
