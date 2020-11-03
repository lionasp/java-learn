package banking;

public class Processor {
    private UserMenu menu;

    public Processor(UserMenu menu) {
        this.menu = menu;
    }

    public void printMenu() {
        this.menu.printMenu();
    }

    public void processOption(int option) {
        this.menu.processOption(option);
    }
}
