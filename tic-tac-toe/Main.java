package tictactoe;

import java.util.Scanner;

public class Main {
    enum GameState {
        NOT_FINISHED("Game not finished"),
        DRAW("Draw"),
        X_WINS("X wins"),
        O_WINS("O wins"),
        IMPOSSIBLE("Impossible");

        String text;

        GameState(String text) {
            this.text = text;
        }
    }

    final static int FIELD_SIZE = 3;

    public static void printField(char[][] field) {
        System.out.println("---------");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private static boolean isPlayerWin(char[][] field, char playerSign) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            boolean horizontal = true;
            boolean vertical = true;
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j] != playerSign) {
                    horizontal = false;
                }
                if (field[j][i] != playerSign) {
                    vertical = false;
                }
            }
            if (horizontal || vertical) {
                return true;
            }
        }

        boolean vertical1 = true;
        boolean vertical2 = true;
        for (int i = 0; i < FIELD_SIZE; i++) {
            if (field[i][i] != playerSign) {
                vertical1 = false;
            }
            if (field[i][FIELD_SIZE - i - 1] != playerSign) {
                vertical2 = false;
            }
        }
        return vertical1 || vertical2;
    }

    public static GameState findState(char[][] field) {
        int xCount = 0;
        int oCount = 0;
        int emptyCount = 0;
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[i][j] == 'X') {
                    xCount++;
                } else if (field[i][j] == 'O') {
                    oCount++;
                } else {
                    emptyCount++;
                }
            }
        }

        if (Math.abs(xCount - oCount) > 1) {
            return GameState.IMPOSSIBLE;
        }

        boolean xWins = isPlayerWin(field, 'X');
        boolean oWins = isPlayerWin(field, 'O');
        if (xWins && oWins) {
            return GameState.IMPOSSIBLE;
        }
        if (xWins) {
            return GameState.X_WINS;
        }
        if (oWins) {
            return GameState.O_WINS;
        }
        if (emptyCount != 0) {
            return GameState.NOT_FINISHED;
        }
        return GameState.DRAW;
    }

    public static void fillField(char[][] field, String cells) {
        for (int i = 0; i < cells.length(); i++) {
            field[i / FIELD_SIZE][i % FIELD_SIZE] = cells.charAt(i);
        }
    }

    public static void main(String[] args) {
        String cells = "_________";
        char[][] field = new char[FIELD_SIZE][FIELD_SIZE];
        fillField(field, cells);
        printField(field);

        Scanner scanner = new Scanner(System.in);
        int a;
        int b;
        boolean firstPlayerTurn = true;
        GameState state = GameState.NOT_FINISHED;

        do {
            System.out.print("Enter the coordinates: ");
            String line = scanner.nextLine();
            String[] split = line.trim().split(" ");
            if (split.length != 2) {
                System.out.println("You should enter numbers!");
                continue;
            }

            // todo: can be improved with 2 for loops
            if (!(split[0].equals("1") || split[0].equals("2") || split[0].equals("3"))) {
                System.out.println("You should enter numbers!");
                continue;
            }

            if (!(split[1].equals("1") || split[1].equals("2") || split[1].equals("3"))) {
                System.out.println("You should enter numbers!");
                continue;
            }

            a = FIELD_SIZE - Integer.parseInt(split[1]);
            b = Integer.parseInt(split[0]) - 1;
            if (field[a][b] == 'O' || field[a][b] == 'X') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            field[a][b] = firstPlayerTurn ? 'X' : 'O';
            firstPlayerTurn = !firstPlayerTurn;
            printField(field);
            state = findState(field);
        } while (state == GameState.NOT_FINISHED);

        System.out.println(state.text);
    }
}
