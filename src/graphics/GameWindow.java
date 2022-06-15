package graphics;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

//    final int scale = 3;

    private static final int blockActualSize = 24;
    public static final int blockSize = 2 * blockActualSize;
    static final int rowsOnScreen = 12;
    static final int columnsOnScreen = 18;
    static final int mapCols = 90;
    static final int screenWidth = blockSize * columnsOnScreen;
    public static final int screenHeight = blockSize * rowsOnScreen;
    public static final int worldWidth = blockSize * mapCols;

    private final GamePanel panel;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.panel = new GamePanel(1);
        this.add(this.panel);
        this.panel.setDoubleBuffered(true);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void update() {
        this.panel.update();
    }

    @Override
    public void repaint() {
        this.panel.repaint();
    }
}