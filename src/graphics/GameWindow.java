package graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame {

    final int scale = 3;

    private final int blockActualSize = 24;
    protected final int blockSize = 3 * blockActualSize;
    protected final int rowsOnScreen = 12;
    protected final int columnsOnScreen = 18;
    protected final int mapCols = 90;
    private final int screenWidth = blockSize * columnsOnScreen;
    private final int screenHeight = blockSize * rowsOnScreen;
    private GamePanel panel;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.panel = new GamePanel(this);
        this.panel.setDoubleBuffered(true);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        try {
            this.panel.setBackgroundImage(ImageIO.read(new File("images/back.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.add(this.panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /*private static Point getCenterLocation(final int width, final int height) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((screen.width - width)/2, (screen.height - height)/2);
    }*/
}

class GamePanel extends JPanel{
    private BufferedImage background;
    private final DrawMap dm;

    GamePanel(GameWindow window){
        super();
        this.dm = new DrawMap(window);
    }

    public void setBackgroundImage(BufferedImage img){
        this.background = img;
    }

    @Override
    public void paint(Graphics gr){
        super.paintComponents(gr);
        Graphics2D g2 = (Graphics2D) gr;
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        dm.paintMap(g2);
        g2.dispose();
    }
}