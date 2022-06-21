package graphics;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements Runnable {

//    final int scale = 3;
    private static final int CLOCK_RATE = 60;
    private static final long NANOSECOND_IN_SECOND = 1000000000L;

    private static final int blockActualSize = 24;
    public static final int blockSize = 2 * blockActualSize;
    static final int rowsOnScreen = 12;
    static final int columnsOnScreen = 17;
    public static final int screenWidth = blockSize * columnsOnScreen;
    public static final int screenHeight = blockSize * rowsOnScreen;

    private GamePanel panel;
    private JPanel control;

    private Thread gameThread;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
//        drawMainMenu();
        drawGame();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.gameThread = new Thread(this);
        this.gameThread.start();
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

    private void update() {
        this.panel.update();
    }

    @Override
    public void repaint() {
        this.panel.repaint();
    }

    @Override
    public void run() {
        double drawInterval = NANOSECOND_IN_SECOND / CLOCK_RATE;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long count = 0;
        while (this.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                count++;
            }
            if (timer >= NANOSECOND_IN_SECOND) {
//                System.out.printf("%d FPS\n", count);
                count = 0;
                timer = 0;
            }
        }
    }
}