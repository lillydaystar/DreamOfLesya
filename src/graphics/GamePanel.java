package graphics;

import creatures.enemies.Creature;
import creatures.player.Cossack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/*
 * This class draws the main game panel, where the map will be displayed
 * and all the characters will be painted.
 */
public class GamePanel extends JPanel {

    public static final int mapCols = 90;
    public static final int mapRows = 0;
    public static final int worldWidth = GameWindow.blockSize * mapCols;
    public static final int worldHeight = GameWindow.blockSize * mapRows;

    private DrawMap dm;
    private BufferedImage background;
    private Cossack cossack;
    /*private int level;*/
    private List<Creature> creatures;

    GamePanel(int level) {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        /*this.level = level;*/
        this.dm = new DrawMap();
        this.cossack = new Cossack();
        creatures = new LinkedList<>();
        this.addKeyListener(new KeyCommander());
        this.revalidate();
        dm.setCossack(this.cossack); //для промальовування карти задаються координати козака
    }

    /*private void nextLevel() {
        this.cossack = null;
        this.background = null;;
        this.dm = new DrawMap();
        loadWorld(level++);
        this.cossack = new Cossack();

    }*/

//    private void loadWorld(int level) {
//        try {
//            switch (level) {
//                case 1: {
//                    this.background = ImageIO.read(new File("images/back.png"));
//                    this.dm.setMapFile(new File("worlds/map1.txt"));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void paintComponent(Graphics graphics) {
        this.revalidate();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        dm.paintMap(graphics2D);
        cossack.draw(graphics2D);
        graphics2D.dispose();
        this.revalidate();
    }

    void update() {
        cossack.update();
    }

    private class KeyCommander implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                cossack.rightPressed();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                cossack.leftPressed();
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_J) {
                cossack.jump();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                cossack.rightReleased();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                cossack.leftReleased();
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_J) {
                cossack.jumpRelease();
            }
        }
    }
}