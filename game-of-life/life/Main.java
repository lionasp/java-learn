package life;

public class Main {

    enum MODE {
        GUI, CLI
    }

    public static void main(String[] args) throws InterruptedException {

        MODE mode = MODE.GUI;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode":
                    try {
                        mode = MODE.valueOf(args[++i]);
                    } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                        System.out.println("Undefined value for parameter \"-mode\"");
                    }
                    break;
                default:
                    break;
            }
        }

        if (mode == MODE.CLI) {
            new GameOfLifeCLI();
        } else {
            new GameOfLife();
        }
    }
}