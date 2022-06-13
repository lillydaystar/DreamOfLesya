package graphics;

import creatures.player.Cossack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/*
 * This class draws the main game panel, where the map will be displayed
 * and all the characters will be painted.
 */
class GamePanel extends JPanel {

    DrawMap dm;
    private BufferedImage background;
    Cossack cossack;

    GamePanel(GameWindow window) {
        this.setPreferredSize(new Dimension(GameWindow.screenWidth, GameWindow.screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyCommander());
        this.setFocusable(true);
        this.dm = window.dm;
        this.cossack = new Cossack();
    }

    public void setBackgroundImage(BufferedImage img){
        this.background = img;
    }

    @Override
    public void paint(Graphics graphics){
        //System.out.println("here paint");
        super.paintComponents(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        dm.paintMap(graphics2D);
        cossack.draw(graphics2D);
        graphics2D.dispose();
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

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_L) {
                cossack.rightPressed();
            }

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_H) {
                cossack.leftPressed();
            }

            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_K) {
                cossack.downPressed();
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_J) {
                cossack.upPressed();
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

            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_K) {
                cossack.downReleased();
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_J) {
                cossack.upReleased();
            }
        }
    }
}