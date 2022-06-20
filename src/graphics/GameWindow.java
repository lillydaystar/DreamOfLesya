package graphics;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

//    final int scale = 3;

    private static final int blockActualSize = 24;
    public static final int blockSize = 2 * blockActualSize;
    static final int rowsOnScreen = 12;
    static final int columnsOnScreen = 17;
    public static final int screenWidth = blockSize * columnsOnScreen;
    public static final int screenHeight = blockSize * rowsOnScreen;

    private GamePanel panel;
    private JPanel control;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
//        drawMainMenu();
        drawGame();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void drawMainMenu() {
        this.control = new FirstPanel();
        this.control.setFocusable(true);
        this.control.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.control);
    }

    private void drawGame() {
        this.panel = new GamePanel(1);
        this.panel.setFocusable(true);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.panel);
    }

    public void update() {
        this.panel.update();
    }

    @Override
    public void repaint() {
        this.panel.repaint();
    }
}