package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame {

//    final int scale = 3;

    private static final int blockActualSize = 24;
    public static final int blockSize = 2 * blockActualSize;
    static final int rowsOnScreen = 12;
    static final int columnsOnScreen = 18;
    static final int mapCols = 90;
    static final int screenWidth = blockSize * columnsOnScreen;
    static final int screenHeight = blockSize * rowsOnScreen;

    private final GamePanel panel;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.panel = new GamePanel();
        this.add(this.panel);
        this.panel.setDoubleBuffered(true);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        try {
            this.panel.setBackgroundImage(ImageIO.read(new File("images/back.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.pack();
        this.setLocationRelativeTo(null);
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