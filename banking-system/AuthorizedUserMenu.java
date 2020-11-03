package banking;

import java.util.Scanner;

public class AuthorizedUserMenu implements UserMenu {
    public void printMenu() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    @Override
    public void processOption(int option) {
        Scanner scanner = new Scanner(System.in);
        switch (option) {
            case 1:
                System.out.println("Balance: " + Auth.getAuthUser().getBalance());
                break;
            case 2:
                System.out.println("Enter income:");
                int income = scanner.nextInt();
                Auth.getAuthUser().setBalance(Auth.getAuthUser().getBalance() + income);
                DbConnector.updateIncome(Auth.getAuthUser());
                System.out.println("Income was added!");
                break;
            case 3:
                System.out.println("Transfer");
                System.out.println("Enter card number:");
                String transferCard = scanner.nextLine();
                if (!Account.validateCardNumber(transferCard)) {
                    System.out.println("Probably you made mistake in the card number.\nPlease try again!");
                    break;
                }

                Account transferAccount = DbConnector.getAccount(transferCard, null);
                if (transferAccount == null) {
                    System.out.println("Such a card does not exist.");
                    break;
                }
                if (transferAccount.getCardNumber().equals(Auth.getAuthUser().getCardNumber())) {
                    System.out.println("You can't transfer money to the same account!");
                    break;
                }

                System.out.println("Enter how much money you want to transfer:");
                int transferAmount = scanner.nextInt();
                if (Auth.getAuthUser().getBalance() < transferAmount) {
                    System.out.println("Not enough money!");
                    break;
                }
                Auth.getAuthUser().setBalance(Auth.getAuthUser().getBalance() - transferAmount);
                DbConnector.updateIncome(Auth.getAuthUser());
                transferAccount.setBalance(transferAccount.getBalance() + transferAmount);
                DbConnector.updateIncome(transferAccount);
                System.out.println("Success!");
                break;
            case 4:
                DbConnector.closeAccount(Auth.getAuthUser());
                Auth.logOut();
                System.out.println("The account has been closed!");
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                Auth.logOut();
                break;
            default:
                break;
        }
    }
}
