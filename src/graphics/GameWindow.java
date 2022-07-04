package graphics;

import main.Music;
import main.Sound;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements Runnable {

    private static final int CLOCK_RATE = 60;
    private static final long NANOSECOND_IN_SECOND = 1000000000L;

    private static final int blockActualSize = 25;
    public static final int blockSize = 2 * blockActualSize;
    static final int rowsOnScreen = 13;
    static final int columnsOnScreen = 17;
    public static final int screenWidth = blockSize * columnsOnScreen;
    public static final int screenHeight = blockSize * rowsOnScreen;

    private GamePanel panel;
    private JPanel control;

    private Thread gameThread;
    private Sound sound;
    private boolean gameOver;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        drawMainMenu();
        this.sound = new Sound();
//        drawGame();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    void drawMainMenu() {
        if(panel != null){
            panel.setFocusable(false);
            this.remove(panel);
            panel = null;
        }
        if(control != null){
            control.setFocusable(false);
            this.remove(control);
            control = null;
        }
        this.control = new FirstPanel(this);
        this.control.setFocusable(true);
        this.control.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.control);
        this.revalidate();
        this.repaint();
    }

    void drawGame() {
        this.gameOver = false;
        if(control != null) {
            this.control.setFocusable(false);
            this.remove(control);
            control = null;
        }
        this.panel = new GamePanel(5);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.panel);
        this.revalidate();
        this.repaint();
        this.panel.setFocusable(true);
        this.panel.requestFocusInWindow();
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    void drawBestiary(){
        if(control != null){
            control.setFocusable(false);
            this.remove(control);
            control = null;
        }
        this.control = new BestiaryPanel(this);
        this.control.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.control);
        this.revalidate();
        this.repaint();
        this.control.setFocusable(true);
        this.control.requestFocusInWindow();
    }

    private void update() {
        this.panel.update();
    }

    private void redraw() {
        this.panel.repaint();
        if(!panel.game)
            gameOver = true;
    }

    private void stopGame() {
        gameThread = null;
        drawMainMenu();
    }

    @Override
    public void run() {
        double drawInterval = NANOSECOND_IN_SECOND / CLOCK_RATE;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        /*long count = 0;*/
        while (this.gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                if(!gameOver) {
                    update();
                    redraw();
                }
                else {
                    stopGame();
                }
                delta--;
                /*count++;*/
            }
            if (timer >= NANOSECOND_IN_SECOND) {
//                System.out.printf("%d FPS\n", count);
                /*count = 0;*/
                timer = 0;
            }
        }
    }
}