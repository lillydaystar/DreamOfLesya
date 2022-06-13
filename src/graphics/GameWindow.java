package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame {

    static final int scale = 2;

    static final int blockActualSize = 24;
    public static final int blockSize = scale * blockActualSize;
    static final int rowsOnScreen = 12;
    static final int columnsOnScreen = 18;
    static final int mapCols = 90;
    public static final int worldWidth = blockSize * mapCols;
    static final int screenWidth = blockSize * columnsOnScreen;
    public static final int screenHeight = blockSize * rowsOnScreen;
    DrawMap dm = new DrawMap(this);
    public GamePanel panel;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.panel = new GamePanel(this);
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