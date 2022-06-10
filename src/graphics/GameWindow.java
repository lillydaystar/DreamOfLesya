package graphics;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {

    final int scale = 3;

    private final int tileSize = 50;
    private final int rowsOnScreen = 12;
    private final int columnsOnScreen = 16;
    private final int screenWidth = tileSize * columnsOnScreen;
    private final int screenHeight = tileSize * rowsOnScreen;

    private JPanel panel;

    public GameWindow() {
        super("Game");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.panel.setBackground(Color.black);

        this.add(this.panel);
        this.pack();
        this.setVisible(true);
    }

    /*private static Point getCenterLocation(final int width, final int height) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((screen.width - width)/2, (screen.height - height)/2);
    }*/
}