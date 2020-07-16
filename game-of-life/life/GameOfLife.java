package life;

import javax.swing.*;
import java.awt.*;


public class GameOfLife extends JFrame {
    final int fieldSize = 20;
    Field field = new Field(fieldSize);

    class Grid extends JComponent {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.darkGray);

            final int cellSize = 13;

            for (int i = 0; i < fieldSize; i++) {
                for (int j = 0; j < fieldSize; j++) {
                    int x = (i + 1) * cellSize;
                    int y = (j + 1) * cellSize;
                    if (field.getField()[i][j] == 'X') {
                        g.drawRect(x, y, cellSize, cellSize);
                    } else {
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                }
            }

        }

    }

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Unhandled error: " + e);
        }
    }

    public GameOfLife() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(285, 340);
        setLocationRelativeTo(null);
        setTitle("Game of Life");
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel generationLabel = new JLabel();
        generationLabel.setText("Generation #0");
        generationLabel.setName("GenerationLabel");
        panel.add(generationLabel);

        JLabel aliveLabel = new JLabel();
        aliveLabel.setName("AliveLabel");
        aliveLabel.setText("Alive:" + field.getAliveCount());
        panel.add(aliveLabel);

        add(panel);
        panel.add(new Grid());

        int i = 0;
        while (isVisible()) {
            delay();
            field.nextGeneration();
            generationLabel.setText("Generation #" + ++i);
            aliveLabel.setText("Alive:" + field.getAliveCount());
            panel.repaint();
        }
    }
}
