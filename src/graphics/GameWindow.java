package graphics;

import creatures.Cossack;
import creatures.params.Health;
import sound.Music;
import sound.Sound;
import javax.sound.sampled.Clip;
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
    private boolean gameOver;
    private Clip anthem;

    public GameWindow() {
        super("Lesya's Dream");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.anthem = Sound.getClip(Music.Background_Anthem);
        Sound.setVolume(this.anthem, 0.15f);
        drawMainMenu();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    void drawMainMenu() {
        stopBackgroundSounds();
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
        if (this.anthem != null) {
            this.anthem.start();
            this.anthem.loop(Clip.LOOP_CONTINUOUSLY);
        }
        this.control.setFocusable(true);
        this.control.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.control);
        this.revalidate();
        this.repaint();
    }

    void drawGame(int lvl) {
        stopBackgroundSounds();
        this.gameOver = false;
        if(control != null) {
            this.control.setFocusable(false);
            this.remove(control);
            control = null;
        }
        this.panel = new GamePanel(lvl);
        this.panel.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.add(this.panel);
        this.revalidate();
        this.repaint();
        this.panel.setFocusable(true);
        this.panel.requestFocusInWindow();
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    void drawBestiary() {
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
        addButtons();
    }

    public void addButtons() {
        final JPanel[] test = {new JPanel(null)};
        test[0].setBounds(0,0, screenWidth, screenHeight);
        JButton restartButton = new JButton("RESTART");
        restartButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        restartButton.addActionListener(press -> {
            test[0].removeAll();
            this.remove(test[0]);
            test[0] = null;
            this.panel.game = true;
            this.gameOver = false;
            restartLevel();
        });
        exitButton.addActionListener(press -> {
            test[0].removeAll();
            this.remove(test[0]);
            test[0] = null;
            drawMainMenu();
        });
        restartButton.setSize(350,70);
        exitButton.setSize(170, 70);
        restartButton.setLocation(110, (GameWindow.screenHeight-100)/2 + 200);
        exitButton.setLocation(590, (GameWindow.screenHeight-100)/2 + 200);
        restartButton.setMargin(new Insets(0, 0, 0, 0));
        restartButton.setBorder(null);
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setBorder(null);
        restartButton.setOpaque(false);
        restartButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        restartButton.setFocusable(false);
        exitButton.setFocusable(false);
        restartButton.setRolloverEnabled(false);
        exitButton.setRolloverEnabled(false);
        try {
            ImageIcon img1 = new ImageIcon("images/Restart.png");
            ImageIcon img2 = new ImageIcon("images/ExitButton.png");
            restartButton.setIcon(img1);
            exitButton.setIcon(img2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        test[0].add(restartButton);
        test[0].add(exitButton);
        this.add(test[0]);
        test[0].setOpaque(true);
        test[0].setBackground(new Color(0,0,0,20));
    }

    private void restartLevel() {
        Cossack cossack = this.panel.getCossack();
        cossack.coins -= panel.getDm().getLevelCoins();
        cossack.alive = true;
        cossack.health = new Health(this.panel.getDm().getLevel());
        cossack.setDefaultCoordinates();
        drawGame(this.panel.getDm().getLevel());
        this.panel.setCossack(cossack);
        this.panel.getDm().setCossack(cossack);
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
                timer = 0;
            }
        }
    }

    private void stopBackgroundSounds() {
        if (this.anthem != null) {
            this.anthem.stop();
            this.anthem.setMicrosecondPosition(0);
        }
    }
}