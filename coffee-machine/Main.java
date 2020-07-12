import java.util.Scanner;

enum MachineState {
    AWAIT, BUY, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS, TAKE_MONEY
}

enum ResultState {
    NONE(0),
    NO_WATER(0),
    NO_MILK(0),
    NO_BEANS(0),
    NO_CUPS(0),
    COFFEE_SOLD(0),
    MONEY_BACK(0);

    public int amount;

    ResultState(int amount) {
        this.amount = amount;
    }
}

enum CoffeeType {
    ESPRESSO(250, 0, 16, 1, 4),
    LATTE(350, 75, 20, 1, 7),
    CAPPUCCINO(200, 100, 12, 1, 6);

    public int water;
    public int milk;
    public int beans;
    public int cups;
    public int money;

    CoffeeType(int water, int milk, int beans, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
    }
}

class Machine {
    public int water;
    public int milk;
    public int beans;
    public int cups;
    public int money;
    private MachineState state;

    Machine(int water, int milk, int beans, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
        this.state = MachineState.AWAIT;
    }

    private ResultState buyCoffee(CoffeeType coffeeType) {
        if (coffeeType.water > water) {
            return ResultState.NO_WATER;
        }
        if (coffeeType.milk > milk) {
            return ResultState.NO_MILK;
        }
        if (coffeeType.beans > beans) {
            return ResultState.NO_BEANS;
        }
        if (coffeeType.cups > cups) {
            return ResultState.NO_CUPS;
        }
        water -= coffeeType.water;
        milk -= coffeeType.milk;
        beans -= coffeeType.beans;
        cups -= coffeeType.cups;
        money += coffeeType.money;
        return ResultState.COFFEE_SOLD;
    }

    public ResultState handle(String action) {
        switch (state) {
            case AWAIT:
                state = MachineState.valueOf(action);
                break;
            case TAKE_MONEY:
                ResultState response = ResultState.MONEY_BACK;
                response.amount = money;
                money = 0;
                state = MachineState.AWAIT;
                return response;
            case BUY:
                CoffeeType coffeeType = CoffeeType.valueOf(action);
                response = buyCoffee(coffeeType);
                state = MachineState.AWAIT;
                return response;
            case FILL_WATER:
                water += Integer.parseInt(action);
                state = MachineState.AWAIT;
                break;
            case FILL_MILK:
                milk += Integer.parseInt(action);
                state = MachineState.AWAIT;
                break;
            case FILL_BEANS:
                beans += Integer.parseInt(action);
                state = MachineState.AWAIT;
                break;
            case FILL_CUPS:
                cups += Integer.parseInt(action);
                state = MachineState.AWAIT;
                break;
            default:
                break;
        }
        return ResultState.NONE;
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Machine machine = new Machine(400, 540, 120, 9, 550);

        boolean timeToStop = false;
        ResultState result;
        while (!timeToStop) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
            String action = scanner.next();
            switch (action) {
                case "remaining":
                    System.out.println("The coffee machine has:");
                    System.out.println(machine.water + " of water");
                    System.out.println(machine.milk + " of milk");
                    System.out.println(machine.beans + " of coffee beans");
                    System.out.println(machine.cups + " of disposable cups");
                    System.out.println("$" + machine.money + " of money");
                    break;
                case "exit":
                    timeToStop = true;
                    break;
                case "fill":
                    System.out.println("Write how many ml of water do you want to add:");
                    machine.handle(MachineState.FILL_WATER.name());
                    machine.handle(scanner.next());
                    System.out.println("Write how many ml of milk do you want to add:");
                    machine.handle(MachineState.FILL_MILK.name());
                    machine.handle(scanner.next());
                    System.out.println("Write how many grams of coffee beans do you want to add:");
                    machine.handle(MachineState.FILL_BEANS.name());
                    machine.handle(scanner.next());
                    System.out.println("Write how many disposable cups of coffee do you want to add:");
                    machine.handle(MachineState.FILL_CUPS.name());
                    machine.handle(scanner.next());
                    break;
                case "buy":
                    System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                    String choice = scanner.next();

                    switch (choice) {
                        case "1":
                            machine.handle(MachineState.BUY.name());
                            result = machine.handle(CoffeeType.ESPRESSO.name());
                            break;
                        case "2":
                            machine.handle(MachineState.BUY.name());
                            result = machine.handle(CoffeeType.LATTE.name());
                            break;
                        case "3":
                            machine.handle(MachineState.BUY.name());
                            result = machine.handle(CoffeeType.CAPPUCCINO.name());
                            break;
                        default:
                            continue;

                    }

                    switch (result) {
                        case NO_WATER:
                            System.out.println("Sorry, not enough water!");
                            break;
                        case NO_BEANS:
                            System.out.println("Sorry, not enough coffee beans!");
                            break;
                        case NO_CUPS:
                            System.out.println("Sorry, not enough disposable cups!");
                            break;
                        case NO_MILK:
                            System.out.println("Sorry, not enough milk!");
                            break;
                        case COFFEE_SOLD:
                            System.out.println("I have enough resources, making you a coffee!");
                            break;
                        default:
                            System.out.println("Unexpected response: " + result.name());
                    }

                    break;
                default:
                    machine.handle(MachineState.TAKE_MONEY.name());
                    result = machine.handle("");
                    System.out.println("I gave you $" + result.amount);
                    break;
            }
            System.out.println();
        }
    }
}
