package graphics;

import javax.swing.*;

public class GameWindow extends JFrame {

    final int scale = 3;

    private static final int tileSize = 50;
    private static final int rowsOnScreen = 12;
    private static final int columnsOnScreen = 16;
    static final int screenWidth = tileSize * columnsOnScreen;
    static final int screenHeight = tileSize * rowsOnScreen;

    private GamePanel panel;

    public GameWindow() {
        super("Game");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.panel = new GamePanel();
        this.add(this.panel);
        this.pack();
        this.setVisible(true);
    }

    public void update() {
        this.panel.update();
    }

   /* @Override
    public void update(Graphics g) {

    }*/

    @Override
    public void repaint() {
        this.panel.repaint();
    }

    /*private static Point getCenterLocation(final int width, final int height) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((screen.width - width)/2, (screen.height - height)/2);
    }*/
}