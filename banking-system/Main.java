package banking;

import java.util.Scanner;

public class Main {

    private static String fetchDbNameFromArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-fileName".equals(args[i]) && args.length > i + 1) {
                return args[i + 1];
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String dbName = fetchDbNameFromArgs(args);
        if (dbName == null) {
            System.out.println("Unknown db name");
            return;
        }
        DbConnector.initDb(dbName);


        int option;
        Scanner scanner = new Scanner(System.in);
        do {
            UserMenu strategy = Auth.getAuthUser() == null ? new AnonymousUserMenu() : new AuthorizedUserMenu();
            Processor processor = new Processor(strategy);
            processor.printMenu();

            option = scanner.nextInt();
            processor.processOption(option);
        } while (option != 0);
        System.out.println("Bye!");
    }
}