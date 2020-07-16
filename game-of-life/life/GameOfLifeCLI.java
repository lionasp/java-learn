package life;

public class GameOfLifeCLI {
    final int fieldSize = 20;
    Field field = new Field(fieldSize);

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Unhandled error: " + e);
        }
    }

    private void printHeader(int generation, int alive) {
        System.out.println("Generation #" + generation);
        System.out.println("Alive: " + alive);
    }

    private void printField() {
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                System.out.print(field.getField()[i][j] == 'X' ? ' ' : 'O');
            }
            System.out.println();
        }
    }

    public GameOfLifeCLI() {
        int i = 0;
        while (true) {
            clearConsole();
            printHeader(i++, field.getAliveCount());
            printField();
            delay();
            field.nextGeneration();
        }
    }
}