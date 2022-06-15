package graphics;

import creatures.player.Cossack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * This class draws the main game panel, where the map will be displayed
 * and all the characters will be painted.
 */
class GamePanel extends JPanel {

    private DrawMap dm;
    private BufferedImage background;
    private Cossack cossack;
    private int level;

    GamePanel() {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyCommander());
        this.setFocusable(true);
        this.level = 1;
        this.dm = new DrawMap();
        loadWorld(level);
        this.cossack = new Cossack();
    }

    private void nextLevel() {
        this.cossack = null;
        this.background = null;;
        this.dm = new DrawMap();
        loadWorld(level++);
        this.cossack = new Cossack();

    }

    private void loadWorld(int level) {
        switch (level) {
            case 1: {
                try {
                    this.background = ImageIO.read(new File("images/back.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.dm.setMapFile(new File("worlds/map1.txt"));
            }
        }
    }

    @Override
    public void paint(Graphics graphics){
        super.paintComponents(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        dm.paintMap(graphics2D);
        cossack.draw(graphics2D);
        graphics2D.dispose();
    }

    void update() {
        cossack.update();
        dm.setCossackWorldX(this.cossack.getWorldX()); //для промальовування карти задається координата козака
        dm.setCossackX(this.cossack.getX());
    }

    private class KeyCommander implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_L) {
                cossack.rightPressed();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_H) {
                cossack.leftPressed();
            }

            if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_J) {
                cossack.jump();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_L) {
                cossack.rightReleased();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_H) {
                cossack.leftReleased();
            }
        }
    }
}