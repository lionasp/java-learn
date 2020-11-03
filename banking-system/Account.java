package banking;

import java.util.concurrent.ThreadLocalRandom;

public class Account {
    private String cardNumber;
    private String pinCode;
    private int balance;

    private Account(String cardNumber, String pinCode) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = 0;
    }

    public Account(String cardNumber, String pinCode, int balance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public static Account generateAccount() {
        final int UPPER_BOUND_PIN_CODE = 9999;
        StringBuilder pinCode = new StringBuilder(String.valueOf(ThreadLocalRandom.current().nextInt(UPPER_BOUND_PIN_CODE)));
        while (pinCode.length() < 4) {
            pinCode.insert(0, "0");
        }
        return new Account(Account.generateCardNumber(), pinCode.toString());
    }

    public static boolean validateCardNumber(String cardNumber) {
        int sum1 = 0;
        int sum2 = 0;
        final int nDigits = cardNumber.length();
        for (int i = nDigits; i > 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i - 1));
            int z = digit;
            int y = digit;
            if (i % 2 != 0) {
                z *= 2;
                if (z > 9) {
                    z -= 9;
                }
                sum1 += z;
            } else sum2 += y;
        }
        int sum = sum1 + sum2;
        if (cardNumber.length() != 16) sum = 1;
        return sum % 10 == 0;
    }

    private static String generateCardNumber() {
        final String IIN = "400000";
        final long UPPER_BOUND_ACCOUNT_NUMBER = 999999999;
        StringBuilder customer_account_number = new StringBuilder(String.valueOf(
                ThreadLocalRandom.current().nextLong(UPPER_BOUND_ACCOUNT_NUMBER))
        );
        while (customer_account_number.length() < 9) {
            customer_account_number.insert(0, "0");
        }
        String rawCardNumber = IIN + customer_account_number.toString();
        int luhnNumbersSum = 0;
        for (int i = 0; i < rawCardNumber.length(); i++) {
            int digit = Character.getNumericValue(rawCardNumber.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            luhnNumbersSum += digit;
        }
        int checksum = 0;
        while ((luhnNumbersSum + checksum) % 10 != 0) {
            checksum++;
        }

        return rawCardNumber + checksum;
    }
}
