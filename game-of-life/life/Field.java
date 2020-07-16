package life;
import java.util.Random;

public class Field {
    char[][] field;

    public Field(int n) {
        Random random = new Random();
        field = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                field[i][j] = random.nextBoolean() ? 'O' : 'X';
            }
        }
    }

    public char[][] getField() {
        return field;
    }

    public int getAliveCount() {
        int counter = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                counter += field[i][j] == 'O' ? 1 : 0;
            }
        }
        return counter;
    }

    private int getNearLifeCount(int i, int j) {
        int lifeCounter = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k == i && l == j) {
                    continue;
                }
                int indK = k < 0 ? field.length - 1 : k;
                int indL = l < 0 ? field.length - 1 : l;
                indK %= field.length;
                indL %= field.length;
                if (field[indK][indL] == 'O') {
                    lifeCounter++;
                }
            }
        }
        return lifeCounter;
    }

    public void nextGeneration() {
        char[][] newField = new char[field.length][field.length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                int lifeCounter = getNearLifeCount(i, j);
                if (lifeCounter == 3) {
                    newField[i][j] = 'O';
                } else if (lifeCounter == 2) {
                    newField[i][j] = field[i][j];
                } else {
                    newField[i][j] = 'X';
                }
            }
        }

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = newField[i][j];
            }
        }
    }
}